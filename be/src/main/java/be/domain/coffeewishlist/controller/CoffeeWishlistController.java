package be.domain.coffeewishlist.controller;

import javax.validation.constraints.Positive;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.domain.coffeewishlist.dto.CoffeeWishlistDto;
import be.domain.coffeewishlist.entity.CoffeeWishlist;
import be.domain.coffeewishlist.mapper.CoffeeWishlistMapper;
import be.domain.coffeewishlist.service.CoffeeWishlistService;
import be.domain.user.dto.MyPageMultiResponseDto;
import be.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping({"/api/coffees", "/api"})
@RequiredArgsConstructor
public class CoffeeWishlistController {
	private final UserService userService;
	private final CoffeeWishlistMapper coffeeWishlistMapper;
	private final CoffeeWishlistService coffeeWishlistService;

	@PatchMapping("/{coffee-id}/wish")
	public ResponseEntity<String> wishCoffee(@PathVariable("coffee-id") @Positive Long coffeeId) {
		coffeeWishlistService.verifyWishState(coffeeId);

		return ResponseEntity.ok("Success to click Wish.");
	}

	@GetMapping("/mypage/wishlist")
	public ResponseEntity<MyPageMultiResponseDto<CoffeeWishlistDto.UserWishlist>> getMyPageCoffee(
		@RequestParam(name = "page", defaultValue = "1") Integer page,
		@RequestParam(name = "size", defaultValue = "10") Integer size) {

		Page<CoffeeWishlist> coffeeWishlists = coffeeWishlistService.getUserWishlist(page, size);
		Page<CoffeeWishlistDto.UserWishlist> responses = coffeeWishlistMapper.coffeesAndWishlistToResponse(coffeeWishlists);

		return ResponseEntity.ok(
			new MyPageMultiResponseDto<>(userService.getLoginUser().getNickname(), responses.getContent(), responses));
	}
}
