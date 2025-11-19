package be.domain.elasticsearch.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;

import be.domain.beer.entity.Beer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "beers")
@Mapping(mappingPath = "elastic/beer-mapping.json")
public class BeerDocument {

	@Id
	private Long id;

	@Field(type = FieldType.Text, analyzer = "nori")
	private String korName;

	@Field(type = FieldType.Text)
	private String engName;

	@Field(type = FieldType.Keyword)
	private String country;

	@Field(type = FieldType.Keyword)
	private String beerCategory;

	@Field(type = FieldType.Double)
	private Double abv;

	@Field(type = FieldType.Keyword)
	private String[] tags;

	@Field(type = FieldType.Double)
	private Double averageStar;

	public static BeerDocument from(Beer beer) {
		String[] beerCategories = beer.getBeerBeerCategories().stream()
			.map(beerBeerCategory -> beerBeerCategory.getBeerCategory().getBeerCategoryType().toString())
			.toArray(String[]::new);

		String[] beerTags = beer.getBeerDetailsTopTags() != null ?
			new String[] {
				beer.getBeerDetailsTopTags().getTag1(),
				beer.getBeerDetailsTopTags().getTag2(),
				beer.getBeerDetailsTopTags().getTag3(),
				beer.getBeerDetailsTopTags().getTag4()
			} : new String[0];

		return BeerDocument.builder()
			.id(beer.getId())
			.korName(beer.getBeerDetailsBasic().getKorName())
			.engName(beer.getBeerDetailsBasic().getEngName())
			.country(beer.getBeerDetailsBasic().getCountry())
			.beerCategory(beerCategories.length > 0 ? beerCategories[0] : null)
			.abv(beer.getBeerDetailsBasic().getAbv())
			.tags(beerTags)
			.averageStar(beer.getBeerDetailsStars().getTotalAverageStars())
			.build();
	}
}
