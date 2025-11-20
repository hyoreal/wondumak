package be.domain.coffeewishlist.repository;

import static be.domain.coffee.entity.QCoffee.*;
import static be.domain.coffeewishlist.entity.QCoffeeWishlist.*;
import static be.domain.user.entity.QUser.*;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffeewishlist.entity.CoffeeWishlist;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CoffeeWishListQRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<CoffeeWishlist> findByUserAndTrue(Long userId, Pageable pageable) {

		List<CoffeeWishlist> coffeeWishlists = jpaQueryFactory.selectFrom(coffeeWishlist)
			.join(coffeeWishlist.coffee, coffee)
			.join(coffeeWishlist.user, user)
			.where(coffeeWishlist.user.id.eq(userId))
			.where(coffeeWishlist.wished.eq(true))
			.orderBy(coffeeWishlist.modifiedAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		int total = jpaQueryFactory
			.selectFrom(coffeeWishlist)
			.join(coffeeWishlist.coffee, coffee)
			.join(coffeeWishlist.user, user)
			.where(coffeeWishlist.user.id.eq(userId))
			.where(coffeeWishlist.wished.eq(true))
			.fetch().size();

		return new PageImpl<>(coffeeWishlists, pageable, total);
	}

	public void deleteByCoffee(Long coffeeId) {

		jpaQueryFactory.delete(coffeeWishlist)
			.where(coffeeWishlist.coffee.id.eq(coffeeId));
	}
}
