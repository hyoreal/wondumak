package be.domain.chat.dto;

import lombok.*;
import java.time.LocalDateTime;

public class ChatDto {

    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MessageRequest {
        private Long roomId;
        private Long senderId; // Added for simplicity in MVP
        private String content;
    }

    @Getter
    @Builder
    public static class MessageResponse {
        private Long messageId;
        private Long roomId;
        private Long senderId;
        private String senderNickname;
        private String content;
        private LocalDateTime createdAt;
    }

    @Getter
    @Builder
    public static class RoomResponse {
        private Long roomId;
        private String title;
    }
}
