package be.global.init;

import be.domain.coffee.entity.CoffeeDetailsCounts;

public class InitConstant {
	public static final CoffeeDetailsCounts BEER_DETAILS_COUNTS =
		CoffeeDetailsCounts.builder()
			// .totalStarCount(50)
			.femaleStarCount(30)
			.maleStarCount(20)
			.ratingCount(15)
			.pairingCount(10)
			.build();
}
