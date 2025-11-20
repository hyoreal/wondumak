package be.domain.coffee.service;

import java.util.List;

import org.springframework.data.domain.Page;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.MonthlyCoffee;
import be.domain.coffee.entity.WeeklyCoffee;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.rating.entity.Rating;

public interface CoffeeService {
	Coffee createCoffee(Coffee coffee);

	Coffee updateCoffee(Coffee coffee, Long coffeeId);

	void deleteCoffee(Long coffeeId);

	void createMonthlyCoffee();

	void createWeeklyCoffee();

	Coffee getCoffee(Long coffeeId);

	List<MonthlyCoffee> findMonthlyCoffees();

	List<WeeklyCoffee> findWeeklyCoffees();

	List<Coffee> findRecommendCoffees();

	Rating findBestRating(Coffee coffee);

	List<CoffeeTag> findTop4CoffeeTags(Coffee coffee);

	String findBestPairingCategory(Coffee coffee);

	List<Coffee> findSimilarCoffees(Long coffeeId);

	List<Rating> findMyRatingWithWishlist();

	Coffee findCoffeeByRatingId(Long ratingId);

	Coffee findCoffeeByPairingId(Long pairingId);

	Coffee findVerifiedCoffee(Long coffeeId);
}
