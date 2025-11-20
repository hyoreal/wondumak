package be.domain.coffee.repository;

import static be.domain.coffee.entity.QCoffee.*;
import static be.domain.coffee.entity.QCoffeeCoffeeTag.*;
import static be.domain.coffeetag.entity.QCoffeeTag.*;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.CoffeeCoffeeTag;
import be.domain.coffeetag.entity.CoffeeTagType;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CoffeeCoffeeTagQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public CoffeeCoffeeTag findCoffeeCoffeeTagByCoffeeAndCoffeeTagType(Coffee findcoffee, CoffeeTagType coffeeTagType) {

		return jpaQueryFactory.selectFrom(coffeeCoffeeTag)
			.join(coffeeCoffeeTag.coffee, coffee)
			.join(coffeeCoffeeTag.coffeeTag, coffeeTag)
			.where(coffeeCoffeeTag.coffee.eq(findcoffee)
				.and(coffeeCoffeeTag.coffeeTag.coffeeTagType.eq(coffeeTagType)))
			.fetchFirst();
	}
}
