package be.global.statistics.service;

import org.springframework.stereotype.Service;

import be.global.statistics.repository.CoffeeCategoryStatisticsQueryRepository;
import be.global.statistics.repository.CoffeeStatisticsQueryRepository;
import be.global.statistics.repository.CoffeeTagStatisticsQueryRepository;
import be.global.statistics.repository.PairingCategoryStatisticsQueryRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StatisticsService {
	private final CoffeeStatisticsQueryRepository coffeeStatisticsQueryRepository;
	private final CoffeeCategoryStatisticsQueryRepository coffeeCategoryStatisticsQueryRepository;
	private final CoffeeTagStatisticsQueryRepository coffeeTagStatisticsQueryRepository;
	private final PairingCategoryStatisticsQueryRepository pairingCategoryStatisticsQueryRepository;

	public void createTotalStatistics() {

	}

	public void createCoffeeStatistics() {
		coffeeStatisticsQueryRepository.createAndSaveCoffeeStatistics();
	}

	public void createCoffeeCategoryStatistics() {
		coffeeCategoryStatisticsQueryRepository.createAndSaveCoffeeCategoryStatistics();
	}

	public void createCoffeeTagStatistics() {
		coffeeTagStatisticsQueryRepository.createAndSaveCoffeeTagStatistics();
	}

	public void createPairingCategoryStatistics() {
		pairingCategoryStatisticsQueryRepository.createAndSaveCoffeeTagStatistics();
	}
}
