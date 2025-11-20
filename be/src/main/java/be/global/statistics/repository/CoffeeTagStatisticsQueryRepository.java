package be.global.statistics.repository;

import static be.domain.coffeetag.entity.QCoffeeTag.*;
import static be.domain.rating.entity.QRating.*;
import static be.domain.user.entity.QUser.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffeetag.repository.CoffeeTagRepository;
import be.domain.pairing.entity.PairingCategory;
import be.domain.user.entity.enums.Gender;
import be.global.statistics.entity.CoffeeTagStatistics;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CoffeeTagStatisticsQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;
	private final CoffeeTagRepository coffeeTagRepository;
	private final CoffeeTagStatisticsRepository coffeeTagStatisticsRepository;

	public void createAndSaveCoffeeTagStatistics() {

		List<Integer> countList =
			jpaQueryFactory.select(coffeeTag.statCount)
				.from(coffeeTag)
				.orderBy(coffeeTag.id.asc())
				.fetch();

		List<Integer> genderCountList = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			genderCountList.add(jpaQueryFactory.selectFrom(rating)
				.join(rating.user, user)
				.where(rating.createdAt.eq(LocalDateTime.now().minusDays(1)))
				.where(rating.user.gender.eq(Gender.values()[i]))
				.fetch().size());
		}

		CoffeeTagStatistics.CoffeeTagStatisticsBuilder coffeeTagStatisticsBuilder = CoffeeTagStatistics.builder();

		coffeeTagStatisticsBuilder.createdAt(LocalDateTime.now());
		coffeeTagStatisticsBuilder.date(LocalDate.now().minusDays(1));
		coffeeTagStatisticsBuilder.week(LocalDate.now().get(WeekFields.ISO.weekOfYear()));
		coffeeTagStatisticsBuilder.straw(countList.get(0));
		coffeeTagStatisticsBuilder.gold(countList.get(1));
		coffeeTagStatisticsBuilder.brown(countList.get(2));
		coffeeTagStatisticsBuilder.black(countList.get(3));
		coffeeTagStatisticsBuilder.sweet(countList.get(4));
		coffeeTagStatisticsBuilder.sour(countList.get(5));
		coffeeTagStatisticsBuilder.bitter(countList.get(6));
		coffeeTagStatisticsBuilder.rough(countList.get(7));
		coffeeTagStatisticsBuilder.fruity(countList.get(8));
		coffeeTagStatisticsBuilder.flower(countList.get(9));
		coffeeTagStatisticsBuilder.malty(countList.get(10));
		coffeeTagStatisticsBuilder.noScent(countList.get(11));
		coffeeTagStatisticsBuilder.weak(countList.get(12));
		coffeeTagStatisticsBuilder.middle(countList.get(13));
		coffeeTagStatisticsBuilder.strong(countList.get(14));
		coffeeTagStatisticsBuilder.noCarbonation(countList.get(15));
		coffeeTagStatisticsBuilder.male(genderCountList.get(0));
		coffeeTagStatisticsBuilder.female(genderCountList.get(1));
		coffeeTagStatisticsBuilder.refuse(genderCountList.get(2));

		coffeeTagStatisticsRepository.save(coffeeTagStatisticsBuilder.build());

		coffeeTagRepository.findAll().stream()
			.forEach(coffeeTag1 -> {
				coffeeTag1.resetStatCount();
				coffeeTagRepository.save(coffeeTag1);
			});
	}
}
