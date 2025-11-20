package be.domain.coffeecategory.dto;

import javax.validation.constraints.NotBlank;

import be.domain.coffeecategory.entity.CoffeeCategoryType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CoffeeCategoryDto {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {

		private Long coffeeCategoryId;
		@NotBlank
		private CoffeeCategoryType coffeeCategoryType;
	}

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class CoffeeResponse {
		@NotBlank
		private CoffeeCategoryType coffeeCategoryType;
	}
}
