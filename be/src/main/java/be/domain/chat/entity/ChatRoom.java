package be.domain.chat.entity;

import be.global.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // For group chat, or null for 1:1

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatRoomUser> chatRoomUsers = new ArrayList<>();

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> messages = new ArrayList<>();

    public void addChatRoomUser(ChatRoomUser chatRoomUser) {
        this.chatRoomUsers.add(chatRoomUser);
        if (chatRoomUser.getChatRoom() != this) {
            chatRoomUser.setChatRoom(this);
        }
    }
}
