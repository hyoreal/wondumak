package be.domain.coffeecategory.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CoffeeCategoryQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	//    public Optional<CoffeeCategory> finCoffeeCategoryByCoffeeCategoryType(CoffeeCategoryType coffeeCategoryType) {
	//
	//    }
}
