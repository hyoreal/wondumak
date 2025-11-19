package be.domain.chat.chatbot.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatbotDto {

	@Getter
	@NoArgsConstructor
	public static class Request {
		private String question;
	}

	@Getter
	@Builder
	public static class Response {
		private String answer;

		public static Response from(String answer) {
			return Response.builder()
				.answer(answer)
				.build();
		}
	}
}
