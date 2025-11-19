package be.domain.chat.chatbot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.domain.chat.chatbot.dto.ChatbotDto;
import be.domain.chat.chatbot.service.ChatbotService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatbot")
public class ChatbotController {

	private final ChatbotService chatbotService;

	@PostMapping("/ask")
	public ResponseEntity<ChatbotDto.Response> askToChatbot(@RequestBody ChatbotDto.Request request) {
		ChatbotDto.Response response = chatbotService.getChatbotResponse(request.getQuestion());
		return ResponseEntity.ok(response);
	}
}
