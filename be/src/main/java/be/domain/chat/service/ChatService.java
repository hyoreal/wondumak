package be.domain.chat.service;

import be.domain.chat.dto.ChatDto;
import be.domain.chat.entity.ChatMessage;
import be.domain.chat.entity.ChatRoom;
import be.domain.chat.entity.ChatRoomUser;
import be.domain.chat.repository.ChatMessageRepository;
import be.domain.chat.repository.ChatRoomRepository;
import be.domain.chat.repository.ChatRoomUserRepository;
import be.domain.user.entity.User;
import be.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomUserRepository chatRoomUserRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final UserService userService;

    @Transactional
    public ChatRoom createRoom(String title, List<Long> userIds) {
        ChatRoom chatRoom = ChatRoom.builder().title(title).build();
        chatRoomRepository.save(chatRoom);

        for (Long userId : userIds) {
            User user = userService.findVerifiedUser(userId);
            ChatRoomUser chatRoomUser = ChatRoomUser.builder()
                    .chatRoom(chatRoom)
                    .user(user)
                    .build();
            chatRoom.addChatRoomUser(chatRoomUser);
            chatRoomUserRepository.save(chatRoomUser);
        }
        return chatRoom;
    }

    @Transactional(readOnly = true)
    public List<ChatDto.RoomResponse> getMyRooms(Long userId) {
        return chatRoomUserRepository.findByUserId(userId).stream()
                .map(cru -> ChatDto.RoomResponse.builder()
                        .roomId(cru.getChatRoom().getId())
                        .title(cru.getChatRoom().getTitle())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public ChatDto.MessageResponse saveMessage(Long roomId, Long userId, String content) {
        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new RuntimeException("Chat room not found"));
        User user = userService.findVerifiedUser(userId);

        ChatMessage message = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(user)
                .content(content)
                .build();

        chatMessageRepository.save(message);

        return ChatDto.MessageResponse.builder()
                .messageId(message.getId())
                .roomId(roomId)
                .senderId(user.getId())
                .senderNickname(user.getNickname())
                .content(content)
                .createdAt(message.getCreatedAt())
                .build();
    }

    @Transactional(readOnly = true)
    public List<ChatDto.MessageResponse> getMessages(Long roomId) {
        return chatMessageRepository.findByChatRoomIdOrderByCreatedAtAsc(roomId).stream()
                .map(msg -> ChatDto.MessageResponse.builder()
                        .messageId(msg.getId())
                        .roomId(roomId)
                        .senderId(msg.getSender().getId())
                        .senderNickname(msg.getSender().getNickname())
                        .content(msg.getContent())
                        .createdAt(msg.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
