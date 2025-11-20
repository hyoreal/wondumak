package be.domain.coffeewishlist.service.pattern;

import org.springframework.transaction.annotation.Transactional;

import be.domain.coffee.entity.Coffee;
import be.domain.coffeewishlist.entity.CoffeeWishlist;
import be.domain.coffeewishlist.repository.CoffeeWishlistRepository;
import be.domain.user.entity.User;

public interface WishState {
	void clickWish(WishButton wishButton, CoffeeWishlistRepository coffeeWishlistRepository, User user, Coffee coffee);
}

class WishStateFalse implements WishState {

	@Override
	@Transactional
	public void clickWish(WishButton wishButton, CoffeeWishlistRepository coffeeWishlistRepository, User user, Coffee coffee) {
		CoffeeWishlist coffeeWishlist = coffeeWishlistRepository.findByCoffeeAndUser(coffee, user);
		CoffeeWishlist saved = CoffeeWishlist.builder()
			.id(coffeeWishlist.getId())
			.coffee(coffee)
			.user(user)
			.wished(true)
			.build();
		coffeeWishlistRepository.save(saved);
		wishButton.setWishState(new WishStateTrue());
	}
}

class WishStateTrue implements WishState {

	@Override
	@Transactional
	public void clickWish(WishButton wishButton, CoffeeWishlistRepository coffeeWishlistRepository, User user, Coffee coffee) {
		CoffeeWishlist coffeeWishlist = coffeeWishlistRepository.findByCoffeeAndUser(coffee, user);
		CoffeeWishlist saved = CoffeeWishlist.builder()
			.id(coffeeWishlist.getId())
			.coffee(coffee)
			.user(user)
			.wished(false)
			.build();
		coffeeWishlistRepository.save(saved);
		wishButton.setWishState(new WishStateFalse());
	}
}