package be.domain.ai.dto;

import lombok.Builder;
import lombok.Getter;
import java.util.List;

@Getter
@Builder
public class AiRecommendationDto {
    private String recommendationText;
    private List<Long> recommendedCoffeeIds;
}
