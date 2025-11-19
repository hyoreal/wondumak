package be.domain.chat.chatbot.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class OpenAiDto {

	@Getter
	@Builder
	public static class Request {
		private String model;
		private List<Message> messages;
	}

	@Getter
	@Builder
	public static class Message {
		private String role;
		private String content;
	}

	@Getter
	@NoArgsConstructor
	public static class Response {
		private List<Choice> choices;
	}

	@Getter
	@NoArgsConstructor
	public static class Choice {
		private Message message;
	}
}
