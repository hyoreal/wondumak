package be.domain.coffee.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import be.domain.coffee.entity.CoffeeDetailsBasic;
import be.domain.coffee.entity.CoffeeDetailsBestRating;
import be.domain.coffee.entity.CoffeeDetailsCounts;
import be.domain.coffee.entity.CoffeeDetailsStars;
import be.domain.coffee.entity.CoffeeDetailsTopTags;
import be.domain.coffeecategory.dto.CoffeeCategoryDto;
import be.domain.coffeecategory.entity.CoffeeCategoryType;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CoffeeDto {

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Post {

		@NotBlank
		private String korName;
		@NotBlank
		private String engName;
		@NotBlank
		private String country;
		@NotNull
		private List<CoffeeCategoryDto.Response> coffeeCategories;
		@NotBlank
		private String thumbnail;
		@NotNull
		private Double roasting; // Was abv
		private Integer acidity; // Was ibu

	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class Patch {

		private String korName;
		private String engName;
		private String country;
		private List<CoffeeCategoryDto.Response> coffeeCategories;
		private String thumbnail;
		private Double roasting;
		private Integer acidity;

	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SearchResponse {

		private Long coffeeId;
		private String korName;
		private String country;
		private List<String> category;
		private Double roasting;
		private Integer acidity;
		private List<String> coffeeDetailsTopTags;
		// private CoffeeDetailsTopTags coffeeDetailsTopTags;
		private Double totalAverageStar;
		private Integer totalStarcount;
		private String thumbnail;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class MonthlyBestResponse {

		private Long monthlyCoffeeId;
		private Long coffeeId;
		private String korName;
		// private List<String> coffeeDetailsTopTags;
		private CoffeeDetailsTopTags coffeeDetailsTopTags;
		private Double totalAverageStars;
		private Integer totalStarCount;
		private String thumbnail;
		private CoffeeDetailsBestRating bestRating;

	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class WeeklyBestResponse {

		private Long weeklyCoffeeId;
		private Long coffeeId;
		private String korName;
		private String thumbnail;
		private List<String> coffeeCategories;
		private String country;
		private Double roasting;
		private Integer acidity;
		private Double averageStar;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class RecommendResponse {

		// private Long weeklyCoffeeId;
		private Long coffeeId;
		private String korName;
		private String thumbnail;
		private List<CoffeeCategoryDto.Response> coffeeCategories;
		private String country;
		private Double roasting;
		private Integer acidity;
		private Double averageStar;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class WishlistResponse {

		private Long coffeeId;
		private String korName;
		private List<CoffeeCategoryDto.CoffeeResponse> coffeeCategories;
		private String thumbnail;
		private String country;
		private Double roasting;
		private Integer acidity;
	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class SimilarResponse {

		private Long coffeeId;
		private String korName;
		private String country;
		private List<CoffeeCategoryDto.CoffeeResponse> coffeeCategories;
		private Double averageStar;
		private Integer starCount;
		private String thumbnail;
		private Double roasting;
		private Integer acidity;

	}

	@Getter
	@Builder
	@NoArgsConstructor(access = AccessLevel.PROTECTED)
	@AllArgsConstructor(access = AccessLevel.PROTECTED)
	public static class DetailsResponse {

		private Long coffeeId;
		private Boolean isWishlist;
		private CoffeeDetailsBasic coffeeDetailsBasic;
		private List<CoffeeCategoryType> coffeeCategoryTypes;
		private List<String> coffeeDetailsTopTags;
		// private CoffeeDetailsTopTags coffeeDetailsTopTags;
		private CoffeeDetailsStars coffeeDetailsStars;
		private CoffeeDetailsCounts coffeeDetailsCounts;
	}
}
