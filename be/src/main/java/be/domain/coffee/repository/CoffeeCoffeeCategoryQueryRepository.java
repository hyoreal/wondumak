package be.domain.coffee.repository;

import static be.domain.coffee.entity.QCoffeeCoffeeCategory.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffee.entity.CoffeeCoffeeCategory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CoffeeCoffeeCategoryQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public void delete(CoffeeCoffeeCategory category) {

		jpaQueryFactory.delete(coffeeCoffeeCategory)
			.where(coffeeCoffeeCategory.eq(category))
			.execute();
	}

	public void deleteList(List<CoffeeCoffeeCategory> coffeeCoffeeCategories) {

		coffeeCoffeeCategories.forEach(category -> {
			jpaQueryFactory.delete(coffeeCoffeeCategory)
				.where(coffeeCoffeeCategory.eq(category))
				.execute();
		});
	}

	public void deleteAllByCoffeeId(Long coffeeId) {

		jpaQueryFactory.delete(coffeeCoffeeCategory)
			.where(coffeeCoffeeCategory.coffee.id.eq(coffeeId))
			.execute();
	}
}
