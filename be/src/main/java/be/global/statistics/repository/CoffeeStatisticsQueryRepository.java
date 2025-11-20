package be.global.statistics.repository;

import static be.domain.coffee.entity.QCoffee.*;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffee.repository.CoffeeRepository;
import be.global.statistics.entity.CoffeeStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CoffeeStatisticsQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private final CoffeeRepository coffeeRepository;
	private final CoffeeStatisticsJdbcRepository coffeeStatisticsJdbcRepository;

	public void createAndSaveCoffeeStatistics() {

		List<CoffeeStatistics> list = new ArrayList<>();

		jpaQueryFactory.selectFrom(coffee)
			.where(coffee.coffeeDetailsStatistics.statViewCount.goe(1))
			.orderBy(coffee.coffeeDetailsStatistics.statViewCount.desc())
			.fetch()
			.forEach(findCoffee -> {
				CoffeeStatistics coffeeStatistics = CoffeeStatistics.builder().build();
				coffeeStatistics.create(findCoffee);

				list.add(coffeeStatistics);

				findCoffee.getCoffeeDetailsStatistics().resetStatistic(); // 통계 초기화
				coffeeRepository.save(findCoffee);
			});

		coffeeStatisticsJdbcRepository.saveAll(list);
	}
}
