package be.domain.coffeewishlist.mapper;

import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import be.domain.coffee.dto.CoffeeDto;
import be.domain.coffeecategory.dto.CoffeeCategoryDto;
import be.domain.coffeewishlist.dto.CoffeeWishlistDto;
import be.domain.coffeewishlist.entity.CoffeeWishlist;

@Mapper(componentModel = "spring")
public interface CoffeeWishlistMapper {

	default Page<CoffeeWishlistDto.UserWishlist> coffeesAndWishlistToResponse(Page<CoffeeWishlist> coffeeWishlists) {

		return new PageImpl<>(coffeeWishlists.stream()
			.map(coffeeWishlist -> CoffeeWishlistDto.UserWishlist.builder()
				.coffee(CoffeeDto.WishlistResponse.builder()
					.coffeeId(coffeeWishlist.getCoffee().getId())
					.korName(coffeeWishlist.getCoffee().getCoffeeDetailsBasic().getKorName())
					.coffeeCategories(coffeeWishlist.getCoffee().getCoffeeCoffeeCategories().stream()
						.map(category -> CoffeeCategoryDto.CoffeeResponse.builder()
							.coffeeCategoryType(category.getCoffeeCategory().getCoffeeCategoryType())
							.build())
						.collect(Collectors.toList()))
					.thumbnail(coffeeWishlist.getCoffee().getCoffeeDetailsBasic().getThumbnail())
					.country(coffeeWishlist.getCoffee().getCoffeeDetailsBasic().getCountry())
					.abv(coffeeWishlist.getCoffee().getCoffeeDetailsBasic().getAbv())
					.ibu(coffeeWishlist.getCoffee().getCoffeeDetailsBasic().getIbu())
					.build())
				.isUserWish(coffeeWishlist.getWished())
				.build()).collect(Collectors.toList()), coffeeWishlists.getPageable(), coffeeWishlists.getTotalElements());
	}
}
