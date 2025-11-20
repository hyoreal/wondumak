package be.domain.coffee.correction;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class ErrorCorrectionController {

	private final ErrorCorrectionService errorCorrectionService;

	@GetMapping("/rating/correct")
	public String ratingCorrect() {
		return errorCorrectionService.correctRatings();
	}

	@GetMapping("/coffee/correct")
	public String correctStars() {
		return errorCorrectionService.correctStars();
	}
}
