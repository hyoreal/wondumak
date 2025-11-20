package be.global.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import be.domain.coffee.service.CoffeeService;
import be.global.statistics.entity.TotalStatistics;
import be.global.statistics.repository.TotalStatisticsRepository;
import be.global.statistics.service.StatisticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class WondumakScheduler {

	private final CoffeeService coffeeService;
	private final StatisticsService statisticsService;
	private final TotalStatisticsRepository totalStatisticsRepository;

	/*
	 * 매달 1일 00시 5분에 이달의 맥주 갱신
	 */
	@Scheduled(cron = "0 5 0 1 * *")
	public void createMonthlyCoffee() {
		coffeeService.createMonthlyCoffee();
	}

	/*
	 * 매주 목요일 00시 5분에 인기 많은 맥주 갱신
	 */
	@Scheduled(cron = "0 5 0 * * THU")
	public void createWeeklyCoffee() {
		coffeeService.createWeeklyCoffee();
	}

	/*
	 * 매일 00시 10분에 직전 일간 통계자료 생성
	 */
	@Scheduled(cron = "0 10 0 * * *")
	public void createCoffeeStatistics() {
		statisticsService.createCoffeeStatistics();
		statisticsService.createCoffeeCategoryStatistics();
		statisticsService.createCoffeeTagStatistics();
		statisticsService.createPairingCategoryStatistics();
	}

	/*
	 * 매일 00시 00분에 직전 일간 통계자료 객체 생성
	 */
	@Scheduled(cron = "0 0 0 * * *")
	public void createTotalStatisticsObject() {
		TotalStatistics totalStatistics =
			TotalStatistics.builder().build();
		totalStatisticsRepository.save(totalStatistics);
	}

}
