package be.domain.ai.service;

import be.domain.ai.dto.AiRecommendationDto;
import be.domain.user.entity.User;
import be.domain.user.entity.UserCoffeeCategory;
import be.domain.user.entity.UserCoffeeTag;
import be.domain.coffee.repository.CoffeeRepository;
import be.domain.coffee.entity.Coffee;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiService {

    private final CoffeeRepository coffeeRepository;

    @Transactional(readOnly = true)
    public AiRecommendationDto getRecommendation(User user) {
        // Simulate AI logic:
        // 1. Analyze user preferences (tags, categories).
        // 2. Find matching coffees.
        // 3. Generate a response text.

        List<String> preferredCategories = user.getUserCoffeeCategories().stream()
                .map(ucc -> ucc.getCoffeeCategory().getCoffeeCategoryType().toString())
                .collect(Collectors.toList());

        List<String> preferredTags = user.getUserCoffeeTags().stream()
                .map(uct -> uct.getCoffeeTag().getCoffeeTagType().toString())
                .collect(Collectors.toList());

        // Simple Mock Logic: Find random 3 coffees or match category
        List<Coffee> recommendedCoffees = coffeeRepository.findAll().stream()
                .limit(3) // Just take 3 for now as mock
                .collect(Collectors.toList());

        // Ideally we would use querydsl to filter by category/tag, but let's keep it simple mock.

        String recommendationText = "Based on your preferences for " +
            (preferredCategories.isEmpty() ? "various coffees" : String.join(", ", preferredCategories)) +
            ", I recommend these coffees for you!";

        return AiRecommendationDto.builder()
                .recommendationText(recommendationText)
                .recommendedCoffeeIds(recommendedCoffees.stream().map(Coffee::getId).collect(Collectors.toList()))
                .build();
    }
}
