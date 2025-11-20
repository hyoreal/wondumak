package be.domain.coffee.repository;

import static be.domain.coffee.entity.QWeeklyCoffee.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffee.entity.WeeklyCoffee;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class WeeklyCoffeeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<WeeklyCoffee> findWeeklyCoffee() {

		return jpaQueryFactory.selectFrom(weeklyCoffee)
			.where(weeklyCoffee.createdAt.month().eq(LocalDateTime.now().getMonthValue()))
			.orderBy(weeklyCoffee.createdAt.desc(), weeklyCoffee.averageStar.desc())
			.limit(5)
			.fetch();
	}
}
