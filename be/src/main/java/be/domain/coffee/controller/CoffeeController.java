package be.domain.coffee.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import be.domain.coffee.dto.CoffeeDto;
import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.MonthlyCoffee;
import be.domain.coffee.entity.WeeklyCoffee;
import be.domain.coffee.mapper.CoffeeMapper;
import be.domain.coffee.service.CoffeeService;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeewishlist.entity.CoffeeWishlist;
import be.domain.coffeewishlist.service.CoffeeWishlistService;
import be.global.dto.MultiResponseDto;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping({"/api/coffees", "/api"})
@RequiredArgsConstructor
public class CoffeeController {
	private final CoffeeMapper coffeeMapper;
	private final CoffeeService coffeeService;
	private final CoffeeWishlistService coffeeWishlistService;

	@PostMapping("/add")
	// @PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<CoffeeDto.DetailsResponse> postCoffee(@Valid @RequestBody CoffeeDto.Post postCoffee) {

		Coffee coffee = coffeeMapper.coffeePostToCoffee(postCoffee);
		Coffee createdCoffee = coffeeService.createCoffee(coffee);
		createdCoffee.addCoffeeCoffeeCategories(coffee.getCoffeeCoffeeCategories()); // Response DTO
		CoffeeDto.DetailsResponse response = coffeeMapper.coffeeToPostDetailsResponse(createdCoffee);

		return ResponseEntity.status(HttpStatus.CREATED).body(response);
	}

	@PatchMapping("/{coffee_id}/edit")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<CoffeeDto.DetailsResponse> patchCoffee(@PathVariable("coffee_id") Long coffeeId,
		@Valid @RequestBody CoffeeDto.Patch patchCoffee) {

		Coffee coffee = coffeeMapper.coffeePatchToCoffee(patchCoffee);
		Coffee updatedCoffee = coffeeService.updateCoffee(coffee, coffeeId);
		updatedCoffee.addCoffeeCoffeeCategories(coffee.getCoffeeCoffeeCategories()); // Response DTO
		CoffeeDto.DetailsResponse response = coffeeMapper.coffeeToPatchDetailsResponse(updatedCoffee);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/{coffee_id}")
	public ResponseEntity<CoffeeDto.DetailsResponse> getCoffee(@PathVariable("coffee_id") Long coffeeId) {

		Coffee coffee = coffeeService.getCoffee(coffeeId);
		List<CoffeeTag> coffeeTags = coffeeService.findTop4CoffeeTags(coffee);
		CoffeeWishlist coffeeWishlist = coffeeWishlistService.getIsWishlist(coffee);
		CoffeeDto.DetailsResponse response = coffeeMapper.coffeeToDetailsResponse(coffee, coffeeTags, coffeeWishlist);

		return ResponseEntity.ok(response);
	}

	@DeleteMapping("/{coffee_id}/delete")
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	public ResponseEntity<String> deleteCoffee(@PathVariable("coffee_id") Long coffeeId) {

		coffeeService.deleteCoffee(coffeeId);
		coffeeWishlistService.deleteWishlist(coffeeId);

		return ResponseEntity.noContent().build();
	}

	//    -----------------------------------------조회 API 세분화---------------------------------------------------

	@GetMapping("/monthly")
	public ResponseEntity<List<CoffeeDto.MonthlyBestResponse>> getMonthlyCoffee() {

		List<MonthlyCoffee> monthlyCoffeeList = coffeeService.findMonthlyCoffees();
		List<CoffeeDto.MonthlyBestResponse> responses =
			coffeeMapper.coffeesToMonthlyBestCoffeeResponse(monthlyCoffeeList);

		return ResponseEntity.ok(responses);
	}

	@GetMapping("/weekly")
	public ResponseEntity<List<CoffeeDto.WeeklyBestResponse>> getWeeklyCoffee() {

		List<WeeklyCoffee> weeklyCoffeeList = coffeeService.findWeeklyCoffees();
		List<CoffeeDto.WeeklyBestResponse> responses =
			coffeeMapper.coffeesToWeeklyBestCoffeeResponse(weeklyCoffeeList);

		return ResponseEntity.ok(responses);
	}

	@GetMapping("/recommend")
	public ResponseEntity<List<CoffeeDto.RecommendResponse>> getRecommendCoffee() {

		List<Coffee> coffeeList = coffeeService.findRecommendCoffees();
		List<CoffeeDto.RecommendResponse> responses = coffeeMapper.coffeesToRecommendResponse(coffeeList);

		return ResponseEntity.ok(responses);
	}

	@GetMapping("/{coffee_id}/similar")
	public ResponseEntity<List<CoffeeDto.SimilarResponse>> getSimilarCoffee(@PathVariable("coffee_id") Long coffeeId) {

		List<Coffee> coffeeList = coffeeService.findSimilarCoffees(coffeeId);
		List<CoffeeDto.SimilarResponse> responses = coffeeMapper.coffeesToSimilarCoffeeResponse(coffeeList);

		return ResponseEntity.ok(responses);
	}
}
