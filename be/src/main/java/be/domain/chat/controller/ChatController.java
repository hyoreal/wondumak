package be.domain.chat.controller;

import be.domain.chat.dto.ChatDto;
import be.domain.chat.service.ChatService;
import be.domain.chat.entity.ChatRoom;
import be.domain.user.entity.User;
import be.domain.user.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;

    // WebSocket Endpoint
    @MessageMapping("/chat/{roomId}")
    @SendTo("/topic/chat/{roomId}")
    public ChatDto.MessageResponse sendMessage(@DestinationVariable Long roomId, ChatDto.MessageRequest request) {
        // In a real app, validate senderId matches the authenticated user
        return chatService.saveMessage(roomId, request.getSenderId(), request.getContent());
    }

    // HTTP Endpoints
    @PostMapping("/api/chat/room")
    public ResponseEntity<ChatRoomResponse> createRoom(@RequestParam String title, @RequestBody List<Long> userIds) {
         ChatRoom room = chatService.createRoom(title, userIds);
         return ResponseEntity.ok(new ChatRoomResponse(room.getId(), room.getTitle()));
    }

    @GetMapping("/api/chat/rooms")
    public ResponseEntity<List<ChatDto.RoomResponse>> getMyRooms() {
        User user = userService.findLoginUser();
        return ResponseEntity.ok(chatService.getMyRooms(user.getId()));
    }

    @GetMapping("/api/chat/{roomId}/messages")
    public ResponseEntity<List<ChatDto.MessageResponse>> getMessages(@PathVariable Long roomId) {
        return ResponseEntity.ok(chatService.getMessages(roomId));
    }

    @Data
    @AllArgsConstructor
    static class ChatRoomResponse {
        private Long roomId;
        private String title;
    }
}
