package be.domain.user.repository;

import static be.domain.coffeecategory.entity.QCoffeeCategory.*;
import static be.domain.user.entity.QUser.*;
import static be.domain.user.entity.QUserCoffeeCategory.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.user.entity.UserCoffeeCategory;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCoffeeCategoryQRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<UserCoffeeCategory> findUserCoffeeCategoryByUserId(Long userId) {

		return jpaQueryFactory.selectFrom(userCoffeeCategory)
			.join(userCoffeeCategory.user, user)
			.join(userCoffeeCategory.coffeeCategory, coffeeCategory)
			.where(userCoffeeCategory.user.id.eq(userId))
			.fetch();
	}

	public void delete(Long userId) {

		jpaQueryFactory.delete(userCoffeeCategory)
			.where(userCoffeeCategory.user.id.eq(userId))
			.execute();
	}

}
