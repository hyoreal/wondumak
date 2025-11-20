package be.domain.elasticsearch.entity;

import javax.persistence.Embedded;
import javax.persistence.Id;

import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.Setting;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.CoffeeDetailsTopTags;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@Document(indexName = "coffee")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Mapping(mappingPath = "elastic/coffee-mapping.json")
@Setting(settingPath = "elastic/coffee-setting.json")
public class CoffeeDocument {

	@Id
	private Long id;
	private String korName;
	private String engName;
	private String thumbnail;
	private String country;
	private String category;
	private Double abv;
	private Integer ibu;
	private Double totalAverageStars;
	private Integer totalStarCount;
	@Embedded
	private CoffeeDetailsTopTags coffeeDetailsTopTags;

	public static CoffeeDocument toEntity(Coffee coffee) {
		return CoffeeDocument.builder()
			.id(coffee.getId())
			.korName(coffee.getCoffeeDetailsBasic().getKorName())
			.engName(coffee.getCoffeeDetailsBasic().getEngName())
			.thumbnail(coffee.getCoffeeDetailsBasic().getThumbnail())
			.country(coffee.getCoffeeDetailsBasic().getCountry())
			.abv(coffee.getCoffeeDetailsBasic().getAbv())
			.ibu(coffee.getCoffeeDetailsBasic().getIbu())
			.totalAverageStars(coffee.getCoffeeDetailsStars().getTotalAverageStars())
			.totalStarCount(coffee.getCoffeeDetailsCounts().getRatingCount())
			.coffeeDetailsTopTags(coffee.getCoffeeDetailsTopTags())
			.build();
	}
}
