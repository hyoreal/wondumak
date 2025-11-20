package be.domain.user.repository;

import static be.domain.coffeetag.entity.QCoffeeTag.*;
import static be.domain.user.entity.QUser.*;
import static be.domain.user.entity.QUserCoffeeTag.*;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.user.entity.UserCoffeeTag;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserCoffeeTagQRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<UserCoffeeTag> findUserCoffeeTagByUserId(Long userId) {

		return jpaQueryFactory.selectFrom(userCoffeeTag)
			.join(userCoffeeTag.user, user)
			.join(userCoffeeTag.coffeeTag, coffeeTag)
			.where(userCoffeeTag.user.id.eq(userId))
			.fetch();
	}

	public void delete(Long userId) {

		jpaQueryFactory.delete(userCoffeeTag)
			.where(userCoffeeTag.user.id.eq(userId))
			.execute();
	}
}
