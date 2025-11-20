package be.domain.coffee.repository;

import static be.domain.coffee.entity.QMonthlyCoffee.*;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffee.entity.MonthlyCoffee;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class MonthlyCoffeeQueryRepository {

	private final JPAQueryFactory jpaQueryFactory;

	public List<MonthlyCoffee> findMonthlyCoffee() {

		return jpaQueryFactory.selectFrom(monthlyCoffee)
			.where(monthlyCoffee.createdAt.month().eq(LocalDateTime.now().getMonthValue()))
			.orderBy(monthlyCoffee.averageStar.desc())
			.limit(5)
			.fetch();
	}
}
