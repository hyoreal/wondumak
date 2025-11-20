package be.domain.coffeewishlist.service;

import org.springframework.data.domain.Page;

import be.domain.coffee.entity.Coffee;
import be.domain.coffeewishlist.entity.CoffeeWishlist;
import be.domain.user.entity.User;

public interface CoffeeWishlistService {
	void createWishlist(Coffee coffee, User user);

	void deleteWishlist(Long coffeeId);

	CoffeeWishlist getIsWishlist(Coffee coffee);

	Page<CoffeeWishlist> getUserWishlist(Integer page, Integer size);

	void wishStatePattern(Coffee coffee, User user);

	void verifyWishState(Long coffeeId);
}
