package be.global.statistics.analysis;

import java.io.IOException;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import be.global.statistics.repository.CoffeeCategoryStatisticsQueryRepository;
import be.global.statistics.repository.CoffeeStatisticsQueryRepository;
import be.global.statistics.repository.CoffeeTagStatisticsQueryRepository;
import be.global.statistics.repository.CoffeeTagStatisticsRepository;
import be.global.statistics.repository.PairingCategoryStatisticsQueryRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/jtable")
@RequiredArgsConstructor
public class JTableSawController {
	private final JTableSawService jTableSawService;
	private final CoffeeTagStatisticsQueryRepository coffeeTagStatisticsQueryRepository;
	private final CoffeeStatisticsQueryRepository coffeeStatisticsQueryRepository;
	private final PairingCategoryStatisticsQueryRepository pairingCategoryStatisticsQueryRepository;
	private final CoffeeCategoryStatisticsQueryRepository coffeeCategoryStatisticsQueryRepository;

	@GetMapping("/test")
	public void test() {
		coffeeStatisticsQueryRepository.createAndSaveCoffeeStatistics();
	}

	@GetMapping("/statistics")
	public void jTableSawTest() throws IOException {
		jTableSawService.statistics();
	}

	@GetMapping("/visualization")
	public void visualization() throws IOException {
		jTableSawService.visualization();
	}

	@GetMapping("/scatterplot")
	public void scatterPlot() throws IOException {
		jTableSawService.scatterPlot();
	}
}
