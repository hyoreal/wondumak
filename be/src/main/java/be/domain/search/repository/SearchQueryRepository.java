package be.domain.search.repository;

import static be.domain.coffee.entity.QCoffee.*;
import static be.domain.coffee.entity.QCoffeeCoffeeCategory.*;
import static be.domain.coffeecategory.entity.QCoffeeCategory.*;
import static be.domain.user.entity.QUser.*;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffee.entity.Coffee;
import be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class SearchQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public Page<Coffee> findCoffeesPageByQueryParam(String queryParam, Pageable pageable) {

		List<Coffee> resultList = new ArrayList<>();

		String[] queryParamArr = queryParam.split(" ");
		StringPath korName = coffee.coffeeDetailsBasic.korName;
		StringPath engName = coffee.coffeeDetailsBasic.engName;

		log.info("####: " + queryParam);

		List<Coffee> fullTextResultList = jpaQueryFactory
			.selectFrom(coffee)
			.where(korName.likeIgnoreCase(queryParam)
				.or(engName.likeIgnoreCase(queryParam)))
			.orderBy(coffee.coffeeDetailsBasic.korName.asc(), coffee.coffeeDetailsBasic.engName.asc())
			.fetch();

		List<Coffee> fullTextContainsResultList = jpaQueryFactory
			.selectFrom(coffee)
			.where(korName.containsIgnoreCase(queryParam)
				.or(engName.containsIgnoreCase(queryParam)))
			.orderBy(coffee.coffeeDetailsBasic.korName.asc(), coffee.coffeeDetailsBasic.engName.asc())
			.fetch();

		log.info("#####: " + fullTextResultList);

		fullTextResultList.addAll(fullTextContainsResultList);

		for (String query : queryParamArr) {

			resultList.addAll(jpaQueryFactory
				.selectFrom(coffee)
				.where(korName.containsIgnoreCase(query).or(engName.containsIgnoreCase(query)))
				.orderBy(coffee.coffeeDetailsBasic.korName.asc(), coffee.coffeeDetailsBasic.engName.asc())
				.fetch());
		}

		resultList = resultList.stream()
			.sorted((a, b) -> (int)((b.getCoffeeDetailsStars().getTotalAverageStars() * 100)
				- (a.getCoffeeDetailsStars().getTotalAverageStars() * 100)))
			.collect(Collectors.toList());

		resultList.addAll(0, fullTextResultList);

		List<Coffee> result = resultList.stream().distinct().collect(Collectors.toList());

		int total = result.size();
		int start = (int)pageable.getOffset();
		int end = Math.min((start + pageable.getPageSize()), total);

		return new PageImpl<>(result.subList(start, end), pageable, total);
	}

	public Page<Coffee> findCoffeesPageByCoffeeCategoryQueryParam(String queryParam, Pageable pageable) {

		queryParam = queryParam.substring(1);

		List<Coffee> coffeeList = jpaQueryFactory
			.selectFrom(coffee)
			.join(coffee.coffeeCoffeeCategories, coffeeCoffeeCategory)
			.join(coffeeCoffeeCategory.coffeeCategory, coffeeCategory)
			.where(coffeeCategory.coffeeCategoryType.stringValue().eq(queryParam))
			.orderBy(coffee.coffeeDetailsStars.totalAverageStars.desc(), coffee.coffeeDetailsBasic.korName.asc(),
				coffee.coffeeDetailsBasic.engName.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(coffee.count())
			.from(coffee)
			.join(coffee.coffeeCoffeeCategories, coffeeCoffeeCategory)
			.join(coffeeCoffeeCategory.coffeeCategory, coffeeCategory)
			.where(coffeeCategory.coffeeCategoryType.stringValue().eq(queryParam))
			.fetchOne();

		return new PageImpl<>(coffeeList, pageable, total);
	}

	public Page<Coffee> findCoffeesPageByCoffeeTagQueryParam(String queryParam, Pageable pageable) {

		queryParam = queryParam.substring(1);

		List<Coffee> coffeeList = jpaQueryFactory
			.selectFrom(coffee)
			.where(coffee.coffeeDetailsTopTags.tag1.eq(queryParam)
				.or(coffee.coffeeDetailsTopTags.tag2.eq(queryParam)))
			.orderBy(coffee.coffeeDetailsStars.totalAverageStars.desc(), coffee.coffeeDetailsBasic.korName.asc(),
				coffee.coffeeDetailsBasic.engName.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(coffee.count())
			.from(coffee)
			.where(coffee.coffeeDetailsTopTags.tag1.eq(queryParam)
				.or(coffee.coffeeDetailsTopTags.tag2.eq(queryParam)))
			.fetchOne();

		return new PageImpl<>(coffeeList, pageable, total);
	}

	public Page<Coffee> findCoffeesPageByPairingCategoryQueryParam(String queryParam, Pageable pageable) {

		queryParam = queryParam.substring(1);

		List<Coffee> coffeeList = jpaQueryFactory
			.selectFrom(coffee)
			.where(coffee.coffeeDetailsStatistics.bestPairingCategory.eq(queryParam))
			.orderBy(coffee.coffeeDetailsStars.totalAverageStars.desc(), coffee.coffeeDetailsBasic.korName.asc(),
				coffee.coffeeDetailsBasic.engName.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(coffee.count())
			.from(coffee)
			.where(coffee.coffeeDetailsStatistics.bestPairingCategory.eq(queryParam))
			.fetchOne();

		return new PageImpl<>(coffeeList, pageable, total);
	}

	public Page<User> findUsersPageByQueryParam(User loginUser, String queryParam, Pageable pageable) {

		queryParam = queryParam.substring(1);

		User findUser = jpaQueryFactory
			.selectFrom(user)
			.where(user.nickname.like(queryParam))
			.fetchOne();

		List<User> userList = jpaQueryFactory
			.selectFrom(user)
			.where(user.nickname.contains(queryParam))
			.orderBy(user.followerCount.desc(), user.nickname.asc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(user.count())
			.from(user)
			.where(user.nickname.contains(queryParam))
			.fetchOne();

		if (findUser != null) {
			userList.add(0, findUser);
			List<User> result = userList.stream().distinct().collect(Collectors.toList());
			return new PageImpl<>(result, pageable, total + 1);
		} else {
			return new PageImpl<>(userList, pageable, total);
		}

	}
}
