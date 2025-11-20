package be.domain.ai.controller;

import be.domain.ai.dto.AiRecommendationDto;
import be.domain.ai.service.AiService;
import be.domain.user.entity.User;
import be.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final AiService aiService;
    private final UserService userService;

    @GetMapping("/recommend")
    public ResponseEntity<AiRecommendationDto> recommend() {
        User user = userService.findLoginUser();
        return ResponseEntity.ok(aiService.getRecommendation(user));
    }
}
