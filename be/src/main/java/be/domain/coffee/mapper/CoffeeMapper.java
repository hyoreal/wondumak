package be.domain.coffee.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.mapstruct.Mapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import be.domain.coffee.dto.CoffeeDto;
import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.CoffeeCoffeeCategory;
import be.domain.coffee.entity.CoffeeDetailsBasic;
import be.domain.coffee.entity.CoffeeDetailsCounts;
import be.domain.coffee.entity.CoffeeDetailsStars;
import be.domain.coffee.entity.MonthlyCoffee;
import be.domain.coffee.entity.WeeklyCoffee;
import be.domain.coffee.service.CoffeeService;
import be.domain.coffeecategory.dto.CoffeeCategoryDto;
import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeewishlist.entity.CoffeeWishlist;

@Mapper(componentModel = "spring")
public interface CoffeeMapper {

	default Coffee coffeePostToCoffee(CoffeeDto.Post postCoffee) {

		CoffeeDetailsBasic coffeeDetailsBasic = postCoffeeToCoffeeDetailsBasic(postCoffee);
		List<CoffeeCoffeeCategory> coffeeCoffeeCategories = getCoffeeCoffeeCategoriesFromResponseDto(postCoffee.getCoffeeCategories());

		return Coffee.builder()
			.coffeeDetailsBasic(coffeeDetailsBasic)
			.coffeeCoffeeCategories(coffeeCoffeeCategories)
			.build();
	}

	default Coffee coffeePatchToCoffee(CoffeeDto.Patch patchCoffee) {

		CoffeeDetailsBasic coffeeDetailsBasic = patchCoffeeToCoffeeDetailsBasic(patchCoffee);
		List<CoffeeCoffeeCategory> coffeeCoffeeCategories = getCoffeeCoffeeCategoriesFromResponseDto(patchCoffee.getCoffeeCategories());

		return Coffee.builder()
			.coffeeDetailsBasic(coffeeDetailsBasic)
			.coffeeCoffeeCategories(coffeeCoffeeCategories)
			.build();
	}

	default CoffeeDto.DetailsResponse coffeeToPostDetailsResponse(Coffee coffee) {

		CoffeeDto.DetailsResponse.DetailsResponseBuilder detailsResponse = CoffeeDto.DetailsResponse.builder();

		detailsResponse.coffeeId(coffee.getId());
		detailsResponse.coffeeDetailsBasic(coffee.getCoffeeDetailsBasic());
		detailsResponse.coffeeDetailsStars(coffee.getCoffeeDetailsStars());
		detailsResponse.coffeeDetailsCounts(coffee.getCoffeeDetailsCounts());
		detailsResponse.coffeeCategoryTypes(coffee.getCoffeeCoffeeCategories().stream()
			.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory()
				.getCoffeeCategoryType())
			.collect(Collectors.toList()));
		//        detailsResponse.coffeeTags(coffeesToSimilarCoffeeResponse(coffee.getSimilarCoffees()));

		return detailsResponse.build();
	}

	default CoffeeDto.DetailsResponse coffeeToPatchDetailsResponse(Coffee coffee) {

		CoffeeDto.DetailsResponse.DetailsResponseBuilder detailsResponse = CoffeeDto.DetailsResponse.builder();

		detailsResponse.coffeeId(coffee.getId());
		detailsResponse.coffeeDetailsBasic(coffee.getCoffeeDetailsBasic());
		detailsResponse.coffeeDetailsStars(coffee.getCoffeeDetailsStars());
		detailsResponse.coffeeDetailsCounts(coffee.getCoffeeDetailsCounts());
		detailsResponse.coffeeDetailsTopTags(coffee.getCoffeeDetailsTopTags().createList());
		// detailsResponse.similarCoffees(coffeesToSimilarCoffeeResponse(coffee.getSimilarCoffees()));
		detailsResponse.coffeeCategoryTypes(coffee.getCoffeeCoffeeCategories().stream()
			.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory()
				.getCoffeeCategoryType())
			.collect(Collectors.toList()));
		//        detailsResponse.coffeeTags(coffeesToSimilarCoffeeResponse(coffee.getSimilarCoffees()));

		return detailsResponse.build();
	}

	default CoffeeDto.DetailsResponse coffeeToDetailsResponse(Coffee coffee, List<CoffeeTag> coffeeTags,
		CoffeeWishlist coffeeWishlist) {

		CoffeeDto.DetailsResponse.DetailsResponseBuilder detailsResponse = CoffeeDto.DetailsResponse.builder();

		detailsResponse.coffeeId(coffee.getId());
		detailsResponse.coffeeDetailsBasic(coffee.getCoffeeDetailsBasic());
		detailsResponse.coffeeDetailsStars(coffee.getCoffeeDetailsStars());
		detailsResponse.coffeeDetailsCounts(coffee.getCoffeeDetailsCounts());

		if (coffeeTags != null && coffeeTags.size() > 3) {
			List<String> tempList = new ArrayList<>();
			for (int i = 0; i < coffeeTags.size(); i++) {
				tempList.add(coffeeTags.get(i).getCoffeeTagType().toString());
				detailsResponse.coffeeDetailsTopTags(tempList);
			}
		}
		detailsResponse.isWishlist(coffeeWishlist != null && coffeeWishlist.getWished());
		// detailsResponse.similarCoffees(coffeesToSimilarCoffeeResponse(coffee.getSimilarCoffees()));
		detailsResponse.coffeeCategoryTypes(coffee.getCoffeeCoffeeCategories().stream()
			.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory()
				.getCoffeeCategoryType())
			.collect(Collectors.toList()));
		//        detailsResponse.coffeeTags(coffeesToSimilarCoffeeResponse(coffee.getSimilarCoffees()));

		return detailsResponse.build();
	}

	default List<CoffeeDto.MonthlyBestResponse> coffeesToMonthlyBestCoffeeResponse(List<MonthlyCoffee> coffeeList) {

		return coffeeList.stream()
			.map(monthlyCoffee -> {

				CoffeeDto.MonthlyBestResponse.MonthlyBestResponseBuilder monthlyBestResponse = CoffeeDto.MonthlyBestResponse.builder();

				monthlyBestResponse.monthlyCoffeeId(monthlyCoffee.getId());
				monthlyBestResponse.coffeeId(monthlyCoffee.getCoffeeId());
				monthlyBestResponse.korName(monthlyCoffee.getKorName());
				monthlyBestResponse.thumbnail(monthlyCoffee.getThumbnail());
				monthlyBestResponse.totalAverageStars(monthlyCoffee.getAverageStar());
				monthlyBestResponse.totalStarCount(monthlyCoffee.getRatingCount());
				monthlyBestResponse.coffeeDetailsTopTags(monthlyCoffee.getCoffeeDetailsTopTags());

				monthlyBestResponse.bestRating(monthlyCoffee.getCoffeeDetailsBestRating());

				return monthlyBestResponse.build();

			}).collect(Collectors.toList());
	}

	default List<CoffeeDto.WeeklyBestResponse> coffeesToWeeklyBestCoffeeResponse(List<WeeklyCoffee> coffeeList) {

		return coffeeList.stream()
			.map(weeklyCoffee -> {

				CoffeeDto.WeeklyBestResponse.WeeklyBestResponseBuilder weeklyBestResponseBuilder = CoffeeDto.WeeklyBestResponse.builder();

				weeklyBestResponseBuilder.weeklyCoffeeId(weeklyCoffee.getId());
				weeklyBestResponseBuilder.coffeeId(weeklyCoffee.getCoffeeId());
				weeklyBestResponseBuilder.korName(weeklyCoffee.getKorName());
				weeklyBestResponseBuilder.thumbnail(weeklyCoffee.getThumbnail());

				weeklyBestResponseBuilder.coffeeCategories(weeklyCoffee.getWeeklyCoffeeCategory().createList());

				weeklyBestResponseBuilder.country(weeklyCoffee.getCountry());
				weeklyBestResponseBuilder.abv(weeklyCoffee.getAbv());
				weeklyBestResponseBuilder.ibu(weeklyCoffee.getIbu());

				weeklyBestResponseBuilder.averageStar(weeklyCoffee.getAverageStar());

				return weeklyBestResponseBuilder.build();

			}).collect(Collectors.toList());
	}

	default List<CoffeeDto.RecommendResponse> coffeesToRecommendResponse(List<Coffee> coffeeList) {

		return coffeeList.stream()
			.map(coffee -> {
				return CoffeeDto.RecommendResponse.builder()
					.coffeeId(coffee.getId())
					.korName(coffee.getCoffeeDetailsBasic().getKorName())
					.thumbnail(coffee.getCoffeeDetailsBasic().getThumbnail())
					.coffeeCategories(coffee.getCoffeeCoffeeCategories().stream()
						.map(category -> CoffeeCategoryDto.Response.builder()
							.coffeeCategoryId(category.getCoffeeCategory().getId())
							.coffeeCategoryType(category.getCoffeeCategory().getCoffeeCategoryType())
							.build())
						.collect(Collectors.toList()))
					.country(coffee.getCoffeeDetailsBasic().getCountry())
					.abv(coffee.getCoffeeDetailsBasic().getAbv())
					.ibu(coffee.getCoffeeDetailsBasic().getIbu())
					.averageStar(coffee.getCoffeeDetailsStars().getTotalAverageStars())
					.build();
			})
			.collect(Collectors.toList());
	}

	default List<CoffeeDto.SimilarResponse> coffeesToSimilarCoffeeResponse(List<Coffee> coffeeList) {

		return coffeeList.stream()
			.map(coffee -> {
				return CoffeeDto.SimilarResponse.builder()
					.coffeeId(coffee.getId())
					.korName(coffee.getCoffeeDetailsBasic().getKorName())
					.country(coffee.getCoffeeDetailsBasic().getCountry())
					.coffeeCategories(coffee.getCoffeeCoffeeCategories().stream()
						.map(category -> CoffeeCategoryDto.CoffeeResponse.builder()
							.coffeeCategoryType(category.getCoffeeCategory().getCoffeeCategoryType())
							.build())
						.collect(Collectors.toList()))
					.averageStar(coffee.getCoffeeDetailsStars().getTotalAverageStars())
					.starCount(coffee.getCoffeeDetailsCounts().getRatingCount())
					.thumbnail(coffee.getCoffeeDetailsBasic().getThumbnail())
					.abv(coffee.getCoffeeDetailsBasic().getAbv())
					.ibu(coffee.getCoffeeDetailsBasic().getIbu())
					.build();
			})
			.collect(Collectors.toList());
	}

	default PageImpl<CoffeeDto.SearchResponse> coffeesPageToSearchResponse(Page<Coffee> coffeePage) {

		return new PageImpl<>(coffeePage.stream()
			.map(coffee -> {

				CoffeeDto.SearchResponse.SearchResponseBuilder searchResponseBuilder = CoffeeDto.SearchResponse.builder();

				searchResponseBuilder.coffeeId(coffee.getId());
				searchResponseBuilder.korName(coffee.getCoffeeDetailsBasic().getKorName());
				searchResponseBuilder.thumbnail(coffee.getCoffeeDetailsBasic().getThumbnail());
				searchResponseBuilder.country(coffee.getCoffeeDetailsBasic().getCountry());
				searchResponseBuilder.category(coffee.getCoffeeCoffeeCategories().stream()
					.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory().getCoffeeCategoryType().toString())
					.collect(Collectors.toList()));
				searchResponseBuilder.abv(coffee.getCoffeeDetailsBasic().getAbv());
				searchResponseBuilder.ibu(coffee.getCoffeeDetailsBasic().getIbu());
				searchResponseBuilder.totalAverageStar(coffee.getCoffeeDetailsStars().getTotalAverageStars());
				searchResponseBuilder.totalStarcount(coffee.getCoffeeDetailsCounts().getRatingCount());
				// searchResponseBuilder.coffeeDetailsTopTags(coffee.getCoffeeDetailsTopTags());

				if (coffee.getCoffeeDetailsTopTags() != null) {
					searchResponseBuilder.coffeeDetailsTopTags(coffee.getCoffeeDetailsTopTags().createList());
				}

				return searchResponseBuilder.build();
			})
			.collect(Collectors.toList()));
	}

	default List<CoffeeDto.SearchResponse> coffeesListToSearchResponse(List<Coffee> coffeeList) {

		return coffeeList.stream()
			.filter(Objects::nonNull)
			.map(coffee -> {

				CoffeeDto.SearchResponse.SearchResponseBuilder searchResponseBuilder = CoffeeDto.SearchResponse.builder();

				searchResponseBuilder.coffeeId(coffee.getId());
				searchResponseBuilder.korName(coffee.getCoffeeDetailsBasic().getKorName());
				searchResponseBuilder.thumbnail(coffee.getCoffeeDetailsBasic().getThumbnail());
				searchResponseBuilder.country(coffee.getCoffeeDetailsBasic().getCountry());
				searchResponseBuilder.category(coffee.getCoffeeCoffeeCategories().stream()
					.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory().getCoffeeCategoryType().toString())
					.collect(Collectors.toList()));
				searchResponseBuilder.abv(coffee.getCoffeeDetailsBasic().getAbv());
				searchResponseBuilder.ibu(coffee.getCoffeeDetailsBasic().getIbu());
				searchResponseBuilder.totalAverageStar(coffee.getCoffeeDetailsStars().getTotalAverageStars());
				searchResponseBuilder.totalStarcount(coffee.getCoffeeDetailsCounts().getRatingCount());
				// searchResponseBuilder.coffeeDetailsTopTags(coffee.getCoffeeDetailsTopTags());

				if (coffee.getCoffeeDetailsTopTags() != null) {
					searchResponseBuilder.coffeeDetailsTopTags(coffee.getCoffeeDetailsTopTags().createList());
				}

				// if (coffeeTags != null && coffeeTags.size() > 3) {
				// 	List<String> tempList = new ArrayList<>();
				// 	for (int i = 0; i < coffeeTags.size(); i++) {
				// 		tempList.add(coffeeTags.get(i).getCoffeeTagType().toString());
				// 		detailsResponse.coffeeDetailsTopTags(tempList);
				// 	}
				// }

				return searchResponseBuilder.build();
			})
			.collect(Collectors.toList());
	}

	private static List<CoffeeCoffeeCategory> getCoffeeCoffeeCategoriesFromResponseDto(
		List<CoffeeCategoryDto.Response> responseList) {
		return responseList.stream()
			.map(response ->
				CoffeeCoffeeCategory.builder()
					.coffeeCategory(CoffeeCategory.builder()
						.id(response.getCoffeeCategoryId())
						.coffeeCategoryType(response.getCoffeeCategoryType())
						.build())
					.build())
			.collect(Collectors.toList());
	}

	private static CoffeeDetailsBasic postCoffeeToCoffeeDetailsBasic(CoffeeDto.Post postCoffee) {

		CoffeeDetailsBasic.CoffeeDetailsBasicBuilder coffeeDetailsBasic = CoffeeDetailsBasic.builder();

		coffeeDetailsBasic.korName(postCoffee.getKorName());
		coffeeDetailsBasic.engName(postCoffee.getEngName());
		coffeeDetailsBasic.country(postCoffee.getCountry());
		coffeeDetailsBasic.thumbnail(postCoffee.getThumbnail());
		coffeeDetailsBasic.abv(postCoffee.getAbv());
		coffeeDetailsBasic.ibu(postCoffee.getIbu());

		return coffeeDetailsBasic.build();
	}

	private static CoffeeDetailsBasic patchCoffeeToCoffeeDetailsBasic(CoffeeDto.Patch patchCoffee) {

		CoffeeDetailsBasic.CoffeeDetailsBasicBuilder coffeeDetailsBasic = CoffeeDetailsBasic.builder();

		coffeeDetailsBasic.korName(patchCoffee.getKorName());
		coffeeDetailsBasic.engName(patchCoffee.getEngName());
		coffeeDetailsBasic.country(patchCoffee.getCountry());
		coffeeDetailsBasic.thumbnail(patchCoffee.getThumbnail());
		coffeeDetailsBasic.abv(patchCoffee.getAbv());
		coffeeDetailsBasic.ibu(patchCoffee.getIbu());

		return coffeeDetailsBasic.build();
	}

	private static CoffeeDetailsStars coffeeToCoffeeDetailsRatings(Coffee coffee) {

		CoffeeDetailsStars.CoffeeDetailsStarsBuilder coffeeDetailsStars = CoffeeDetailsStars.builder();

		coffeeDetailsStars.totalAverageStars(coffee.getCoffeeDetailsStars().getTotalAverageStars());
		coffeeDetailsStars.femaleAverageStars(coffee.getCoffeeDetailsStars().getFemaleAverageStars());
		coffeeDetailsStars.maleAverageStars(coffee.getCoffeeDetailsStars().getMaleAverageStars());

		return coffeeDetailsStars.build();
	}

	private static CoffeeDetailsCounts coffeeToCoffeeDetailsCounts(Coffee coffee) {

		CoffeeDetailsCounts.CoffeeDetailsCountsBuilder coffeeDetailsCounts = CoffeeDetailsCounts.builder();

		// coffeeDetailsCounts.totalStarCount(coffee.getCoffeeDetailsCounts().getTotalStarCount());
		coffeeDetailsCounts.femaleStarCount(coffee.getCoffeeDetailsCounts().getFemaleStarCount());
		coffeeDetailsCounts.maleStarCount(coffee.getCoffeeDetailsCounts().getMaleStarCount());
		coffeeDetailsCounts.ratingCount(coffee.getCoffeeDetailsCounts().getRatingCount());
		coffeeDetailsCounts.pairingCount(coffee.getCoffeeDetailsCounts().getPairingCount());

		return coffeeDetailsCounts.build();
	}
}
