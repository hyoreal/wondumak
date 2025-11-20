package be.domain.coffeewishlist.dto;

import be.domain.coffee.dto.CoffeeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class CoffeeWishlistDto {

	@Getter
	@Builder
	@AllArgsConstructor
	public static class UserWishlist {
		private CoffeeDto.WishlistResponse coffee;
		private Boolean isUserWish;
	}
}
