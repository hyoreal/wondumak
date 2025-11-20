package be.global.statistics.repository;

import static be.domain.coffeecategory.entity.QCoffeeCategory.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffeecategory.repository.CoffeeCategoryRepository;
import be.global.statistics.entity.CoffeeCategoryStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CoffeeCategoryStatisticsQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private final CoffeeCategoryRepository coffeeCategoryRepository;
	private final CoffeeCategoryStatisticsRepository coffeeCategoryStatisticsRepository;

	public void createAndSaveCoffeeCategoryStatistics() {

		List<Integer> countList =
			jpaQueryFactory.select(coffeeCategory.statCount)
				.from(coffeeCategory)
				.orderBy(coffeeCategory.id.asc())
				.fetch();

		CoffeeCategoryStatistics.CoffeeCategoryStatisticsBuilder coffeeCategoryStatisticsBuilder = CoffeeCategoryStatistics.builder();

		coffeeCategoryStatisticsBuilder.createdAt(LocalDateTime.now());
		coffeeCategoryStatisticsBuilder.date(LocalDate.now().minusDays(1));
		coffeeCategoryStatisticsBuilder.week(LocalDate.now().get(WeekFields.ISO.weekOfYear()));
		coffeeCategoryStatisticsBuilder.ale(countList.get(0));
		coffeeCategoryStatisticsBuilder.lager(countList.get(1));
		coffeeCategoryStatisticsBuilder.weizen(countList.get(2));
		coffeeCategoryStatisticsBuilder.dunkel(countList.get(3));
		coffeeCategoryStatisticsBuilder.pilsener(countList.get(4));
		coffeeCategoryStatisticsBuilder.fruitCoffee(countList.get(5));
		coffeeCategoryStatisticsBuilder.nonAlcoholic(countList.get(6));
		coffeeCategoryStatisticsBuilder.etc(countList.get(7));

		coffeeCategoryStatisticsRepository.save(coffeeCategoryStatisticsBuilder.build());

		coffeeCategoryRepository.findAll().stream()
			.forEach(coffeeCategory1 -> {
				coffeeCategory1.resetStatCount();
				coffeeCategoryRepository.save(coffeeCategory1);
			});
	}
}
