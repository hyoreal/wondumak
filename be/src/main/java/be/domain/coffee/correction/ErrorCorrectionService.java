package be.domain.coffee.correction;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.repository.CoffeeQueryRepository;
import be.domain.coffee.repository.CoffeeRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ErrorCorrectionService {

	private final CoffeeRepository coffeeRepository;
	private final CoffeeQueryRepository coffeeQueryRepository;

	@Transactional
	public String correctRatings() {

		List<Coffee> coffeeList = coffeeRepository.findAll();

		coffeeList.stream()
			// .filter(coffee -> coffee.getRatingList().size() != 0)
			.forEach(coffee -> {

				if (coffee.getRatingList().size() == 0) {
					coffee.correct(0, 0.0, 0,
						0.0, 0, 0.0);
				}

				List<Long> values = coffeeQueryRepository.findRatingsCounts(coffee);

				Integer totalRatingCount = Integer.parseInt(String.valueOf(values.get(0)));
				Integer totalFemaleRatingCount = Integer.parseInt(String.valueOf(values.get(1)));
				Integer totalMaleRatingCount = Integer.parseInt(String.valueOf(values.get(2)));

				coffee.correct(totalRatingCount, totalFemaleRatingCount, totalMaleRatingCount);

				coffeeRepository.save(coffee);
			});

		return "Corrected";
	}

	@Transactional
	public String correctStars() {

		List<Coffee> coffeeList = coffeeRepository.findAll();

		coffeeList.stream()
			.filter(coffee -> coffee.getRatingList().size() != 0)
			.forEach(coffee -> {
				List values = coffeeQueryRepository.findStarsAndCounts(coffee);

				Double totalStar = (Double)values.get(0);
				Integer totalStarCount = Integer.parseInt(String.valueOf(values.get(1)));
				Double totalAverageStars = (double)(Math.round(totalStar * 100 / totalStarCount)) / 100;

				Double totalFemaleStar = (Double)values.get(2);
				Integer totalFemaleStarCount = Integer.parseInt(String.valueOf(values.get(3)));
				Double totalFemaleAverageStars =
					(double)(Math.round(totalFemaleStar * 100 / totalFemaleStarCount)) / 100;

				Double totalMaleStar = (Double)values.get(4);
				Integer totalMaleStarCount = Integer.parseInt(String.valueOf(values.get(5)));
				Double totalMaleAverageStars = (double)(Math.round(totalMaleStar * 100 / totalMaleStarCount)) / 100;

				coffee.correct(totalStarCount, totalAverageStars,
					totalFemaleStarCount, totalFemaleAverageStars,
					totalMaleStarCount, totalMaleAverageStars);

				coffeeRepository.save(coffee);
			});
		return "Corrected";
	}
}
