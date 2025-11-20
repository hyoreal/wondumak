package be.domain.coffee.repository;

import static be.domain.coffee.entity.QCoffee.*;
import static be.domain.coffee.entity.QCoffeeCoffeeCategory.*;
import static be.domain.coffee.entity.QCoffeeCoffeeTag.*;
import static be.domain.coffeecategory.entity.QCoffeeCategory.*;
import static be.domain.coffeetag.entity.QCoffeeTag.*;
import static be.domain.coffeewishlist.entity.QCoffeeWishlist.*;
import static be.domain.pairing.entity.QPairing.*;
import static be.domain.rating.entity.QRating.*;
import static be.domain.user.entity.QUser.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import be.domain.coffee.entity.Coffee;
import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.rating.entity.Rating;
import be.domain.user.entity.User;
import be.domain.user.entity.UserCoffeeCategory;
import be.domain.user.entity.enums.Gender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CoffeeQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

	public List<Coffee> findMonthlyCoffee() {

		return jpaQueryFactory
			.selectFrom(coffee)
			.join(coffee.ratingList, rating)
			.where(rating.createdAt.month().eq(LocalDateTime.now().getMonthValue() - 1))
			.orderBy(rating.star.avg().desc())
			// .orderBy(coffee.coffeeDetailsStars.totalAverageStars.desc())
			.limit(5)
			.fetch();
	}

	public List<Coffee> findWeeklyCoffee() {

		return jpaQueryFactory
			.selectFrom(coffee)
			.join(coffee.ratingList, rating)
			.where(rating.createdAt
				.between(LocalDate.now().minusDays(7).atStartOfDay(), LocalDate.now().atStartOfDay()))
			.orderBy(rating.star.avg().desc())
			// .orderBy(coffee.coffeeDetailsStars.totalAverageStars.desc())
			.limit(5)
			.fetch();
	}

	public List<Coffee> findRecommendCoffee(User findUser) {

		List<CoffeeCategory> coffeeCategoryList =
			findUser.getUserCoffeeCategories().stream()
				.map(UserCoffeeCategory::getCoffeeCategory)
				.collect(Collectors.toList());

		List<Coffee> result = new ArrayList<>();

		for (CoffeeCategory category : coffeeCategoryList) {

			List<Coffee> coffeeList = jpaQueryFactory.selectFrom(coffee)
				.join(coffee.coffeeCoffeeCategories, coffeeCoffeeCategory)
				.join(coffeeCoffeeCategory.coffeeCategory, coffeeCategory)
				.where(coffeeCategory.eq(category))
				.fetch();

			result.addAll(coffeeList);
		}

		return result.stream()
			.distinct()
			.sorted((a, b) -> (int)((b.getCoffeeDetailsStars().getTotalAverageStars() * 100)
				- (a.getCoffeeDetailsStars().getTotalAverageStars() * 100)))
			.limit(5)
			.collect(Collectors.toList());
	}

	public List<CoffeeTag> findTop4CoffeeTag(Coffee findCoffee) {

		return jpaQueryFactory
			.selectFrom(coffeeTag)
			.join(coffeeTag.coffeeCoffeeTags, coffeeCoffeeTag)
			.where(coffeeCoffeeTag.coffee.eq(findCoffee))
			.groupBy(coffeeTag)
			.orderBy(coffeeTag.count().desc())
			.limit(4)
			.fetch();
	}

	public String findBestPairingCategory(Coffee findCoffee) {

		return jpaQueryFactory
			.select(pairing.pairingCategory.stringValue())
			.from(pairing)
			.where(pairing.coffee.eq(findCoffee))
			.groupBy(pairing.pairingCategory)
			.orderBy(pairing.pairingCategory.stringValue().count().desc())
			.fetchFirst();
	}

	public List<Coffee> findSimilarCoffee(Coffee findCoffee) {

		List<String> coffeeCategories = findCoffee.getCoffeeCoffeeCategories().stream()
			.map(coffeeCoffeeCategory1 -> coffeeCoffeeCategory1.getCoffeeCategory().getCoffeeCategoryType().toString())
			.collect(Collectors.toList());

		List<Coffee> result = new ArrayList<>();
		List<Coffee> coffeeList = new ArrayList<>();

		if (findCoffee.getCoffeeDetailsTopTags() != null) {
			coffeeList = jpaQueryFactory
				.selectFrom(coffee)
				.join(coffee.coffeeCoffeeCategories, coffeeCoffeeCategory)
				.join(coffeeCoffeeCategory.coffeeCategory, coffeeCategory)
				.where(coffee.ne(findCoffee))
				.where(coffeeCategory.coffeeCategoryType.stringValue().eq(coffeeCategories.get(0))
					.and(coffee.coffeeDetailsTopTags.tag1.eq(findCoffee.getCoffeeDetailsTopTags().getTag1())
						.or(coffee.coffeeDetailsTopTags.tag1.eq(findCoffee.getCoffeeDetailsTopTags().getTag2())))
					.or(coffee.coffeeDetailsTopTags.tag2.eq(findCoffee.getCoffeeDetailsTopTags().getTag1())
						.or(coffee.coffeeDetailsTopTags.tag2.eq(findCoffee.getCoffeeDetailsTopTags().getTag2()))))
				.orderBy(coffee.coffeeDetailsStars.totalAverageStars.desc())
				.limit(5)
				.fetch();
		}

		if (coffeeList.size() < 5) {
			List<Coffee> tempList = jpaQueryFactory
				.selectFrom(coffee)
				.join(coffee.coffeeCoffeeCategories, coffeeCoffeeCategory)
				.join(coffeeCoffeeCategory.coffeeCategory, coffeeCategory)
				.where(coffee.ne(findCoffee))
				.where(coffeeCategory.coffeeCategoryType.stringValue().eq(coffeeCategories.get(0)))
				.orderBy(coffee.coffeeDetailsStars.totalAverageStars.desc())
				.limit(5 - coffeeList.size())
				.fetch();

			coffeeList.addAll(tempList);
		}

		result.addAll(coffeeList);

		return result;
	}

	public Coffee findCoffeeByRatingId(Long ratingId) {

		return jpaQueryFactory
			.selectFrom(coffee)
			.join(coffee.ratingList, rating)
			.where(rating.id.eq(ratingId))
			.fetchFirst();
	}

	public Coffee findCoffeeByPairingId(Long pairingId) {

		return jpaQueryFactory
			.selectFrom(coffee)
			.join(coffee.pairingList, pairing)
			.where(pairing.id.eq(pairingId))
			.fetchFirst();
	}

	public Rating findBestRating(Coffee findCoffee) {

		return jpaQueryFactory.selectFrom(rating)
			.where(rating.coffee.eq(findCoffee))
			.fetchFirst();
	}

	public Page<Coffee> findMyPageCoffees(User loginUser, Pageable pageable) {

		List<Coffee> coffeeList = jpaQueryFactory.select(coffee)
			.join(coffee.coffeeWishlists, coffeeWishlist)
			.join(coffeeWishlist.user, user)
			.from(coffee)
			.where(coffeeWishlist.user.eq(loginUser))
			.orderBy(coffeeWishlist.createdAt.desc())
			.offset(pageable.getOffset())
			.limit(pageable.getPageSize())
			.fetch();

		Long total = jpaQueryFactory
			.select(coffee.count())
			.from(coffee)
			.join(coffee.coffeeWishlists, coffeeWishlist)
			.join(coffeeWishlist.user, user)
			.where(coffeeWishlist.user.eq(loginUser))
			.fetchOne();

		return new PageImpl<>(coffeeList, pageable, total);
	}

	public List<Rating> findMyRatingWithWishlist(User loginUser) {

		return jpaQueryFactory.select(rating)
			.join(coffee.coffeeWishlists, coffeeWishlist)
			.join(coffee.ratingList, rating)
			.join(coffeeWishlist.user, user)
			.from(rating)
			.where(coffeeWishlist.user.eq(loginUser))
			.fetch();
	}

	public List<Coffee> findRatedCoffeesListByUserId(Long userId) {

		return jpaQueryFactory.selectFrom(coffee)
			.join(coffee.ratingList, rating)
			.where(rating.user.id.eq(userId))
			.fetch();
	}

	public List<Coffee> findCoffeesListByImage(List<String> engNameList) {

		List<Coffee> result = new ArrayList<>();

		for (String engName : engNameList) {
			result.add(jpaQueryFactory.selectFrom(coffee)
				.where(coffee.coffeeDetailsBasic.engName.eq(engName))
				.fetchFirst());
		}
		return result;
	}

	//    --------------------------------------------------------------------------------------------

	public List<Long> findRatingsCounts(Coffee findCoffee) {

		Long totalRatingCount = Objects.requireNonNull(jpaQueryFactory.select(rating.count())
			.from(rating)
			.where(rating.coffee.eq(findCoffee))
			.fetchOne());

		Long totalFemaleRatingCount = Objects.requireNonNull(
			jpaQueryFactory.select(rating.count())
				.from(rating)
				.join(rating.user, user)
				.where(rating.coffee.eq(findCoffee).and(rating.user.gender.eq(Gender.FEMALE)))
				.fetchOne());

		Long totalMaleRatingCount = Objects.requireNonNull(
			jpaQueryFactory.select(rating.count())
				.from(rating)
				.join(rating.user, user)
				.where(rating.coffee.eq(findCoffee).and(rating.user.gender.eq(Gender.MALE)))
				.fetchOne());

		return new ArrayList<>(List.of(totalRatingCount, totalFemaleRatingCount, totalMaleRatingCount));
	}

	public List<?> findStarsAndCounts(Coffee findCoffee) {

		List<Double> totalStarList = jpaQueryFactory.select(rating.star)
			.from(rating)
			.where(rating.coffee.eq(findCoffee))
			.fetch();

		Long totalStarCount = jpaQueryFactory.select(rating.count())
			.from(rating)
			.where(rating.coffee.eq(findCoffee))
			.fetchOne();

		List<Double> totalFemaleStarList = jpaQueryFactory.select(rating.star)
			.from(rating)
			.where(rating.user.gender.eq(Gender.FEMALE).and(rating.coffee.eq(findCoffee)))
			.fetch();

		Long totalFemaleStarCount = jpaQueryFactory.select(rating.count())
			.from(rating)
			.where(rating.user.gender.eq(Gender.FEMALE).and(rating.coffee.eq(findCoffee)))
			.fetchOne();

		List<Double> totalMaleStarList = jpaQueryFactory.select(rating.star)
			.from(rating)
			.where(rating.user.gender.eq(Gender.MALE).and(rating.coffee.eq(findCoffee)))
			.fetch();

		Long totalMaleStarCount = jpaQueryFactory.select(rating.count())
			.from(rating)
			.where(rating.user.gender.eq(Gender.MALE).and(rating.coffee.eq(findCoffee)))
			.fetchOne();

		Double totalStar;
		Double totalFemaleStar;
		Double totalMaleStar;

		if (totalStarList.size() != 0) {
			totalStar = totalStarList.stream().mapToDouble(Double::doubleValue).sum();
		} else {
			totalStar = 0.0;
		}

		if (totalFemaleStarList.size() != 0) {
			totalFemaleStar = totalFemaleStarList.stream().mapToDouble(Double::doubleValue).sum();
		} else {
			totalFemaleStar = 0.0;
		}

		if (totalMaleStarList.size() != 0) {
			totalMaleStar = totalMaleStarList.stream().mapToDouble(Double::doubleValue).sum();
		} else {
			totalMaleStar = 0.0;
		}

		List<?> result = new ArrayList<>(List.of(totalStar, totalStarCount,
			totalFemaleStar, totalFemaleStarCount, totalMaleStar, totalMaleStarCount));

		return result;
	}

}
