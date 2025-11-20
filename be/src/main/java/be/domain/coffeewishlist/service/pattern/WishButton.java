package be.domain.coffeewishlist.service.pattern;

import be.domain.coffee.entity.Coffee;
import be.domain.coffeewishlist.repository.CoffeeWishlistRepository;
import be.domain.user.entity.User;

public class WishButton {
	private WishState wishState = new WishStateTrue();

	public void setWishState(WishState wishState) {
		this.wishState = wishState;
	}

	public void clickButton(WishButton wishButton, CoffeeWishlistRepository coffeeWishlistRepository, User user, Coffee coffee) {
		wishState.clickWish(wishButton, coffeeWishlistRepository, user, coffee);
	}
}
