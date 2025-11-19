package be.domain.chat.chatbot.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import be.domain.chat.chatbot.dto.ChatbotDto;
import be.domain.chat.chatbot.dto.OpenAiDto;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ChatbotService {

	private final WebClient webClient;

	@Value("${openai.api.key}")
	private String apiKey;

	@Value("${openai.api.url}")
	private String apiUrl;

	@Value("${openai.model}")
	private String model;

	public ChatbotDto.Response getChatbotResponse(String question) {
		String prompt = "당신은 맥주를 추천해주는 '비어 소믈리에' 챗봇입니다. 사용자의 질문에 대해 친절하고 상세하게 답변해주세요. "
			+ "모르는 정보에 대해서는 솔직하게 모른다고 답변해야 합니다. \n\n질문: " + question;

		OpenAiDto.Request request = OpenAiDto.Request.builder()
			.model(model)
			.messages(List.of(
				OpenAiDto.Message.builder().role("system").content("You are a helpful beer sommelier assistant.").build(),
				OpenAiDto.Message.builder().role("user").content(prompt).build()
			))
			.build();

		OpenAiDto.Response openAiResponse = webClient.post()
			.uri(apiUrl)
			.header(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
			.contentType(MediaType.APPLICATION_JSON)
			.body(Mono.just(request), OpenAiDto.Request.class)
			.retrieve()
			.bodyToMono(OpenAiDto.Response.class)
			.block();

		String answer = "죄송합니다. 답변을 생성하는 데 문제가 발생했습니다.";
		if (openAiResponse != null && openAiResponse.getChoices() != null && !openAiResponse.getChoices().isEmpty()) {
			answer = openAiResponse.getChoices().get(0).getMessage().getContent();
		}

		return ChatbotDto.Response.from(answer);
	}
}
