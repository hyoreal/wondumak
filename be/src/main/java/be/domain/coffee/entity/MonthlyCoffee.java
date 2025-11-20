package be.domain.coffee.entity;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import be.domain.coffeetag.entity.CoffeeTag;
import be.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@Builder
@ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MonthlyCoffee extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@Column(name = "monthly_coffee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long coffeeId;
	private String korName;
	// private String country;
	private String thumbnail;
	// @Embedded
	// private MonthlyCoffeeCategory monthlyCoffeeCategory;
	@Embedded
	private CoffeeDetailsTopTags coffeeDetailsTopTags;
	// private Double abv;
	// private Integer ibu;
	private Double averageStar;
	private Integer ratingCount;
	@Embedded
	private CoffeeDetailsBestRating coffeeDetailsBestRating;

	public void create(Coffee coffee) {
		this.coffeeId = coffee.getId();
		this.korName = coffee.getCoffeeDetailsBasic().getKorName();
		this.thumbnail = coffee.getCoffeeDetailsBasic().getThumbnail();
		this.coffeeDetailsTopTags = coffee.getCoffeeDetailsTopTags();
		this.averageStar = coffee.getCoffeeDetailsStars().getTotalAverageStars();
		this.ratingCount = coffee.getCoffeeDetailsCounts().getRatingCount();
		this.coffeeDetailsBestRating = coffee.getCoffeeDetailsBestRating();
	}

	public List<String> createTagList() {
		return List.of(this.coffeeDetailsTopTags.getTag1(), this.coffeeDetailsTopTags.getTag2(),
			this.coffeeDetailsTopTags.getTag3(), this.coffeeDetailsTopTags.getTag4());
	}

	public void addBestRating(CoffeeDetailsBestRating coffeeDetailsBestRating) {
		this.coffeeDetailsBestRating = coffeeDetailsBestRating;
	}

	// public void create(Coffee coffee) {
	// 	this.id = coffee.getId();
	// 	this.korName = coffee.getCoffeeDetailsBasic().getKorName();
	// 	this.country = coffee.getCoffeeDetailsBasic().getCountry();
	// 	this.thumbnail = coffee.getCoffeeDetailsBasic().getThumbnail();
	// 	List<String> categoryString = coffee.getCoffeeCoffeeCategories().stream()
	// 		.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory().toString())
	// 		.collect(Collectors.toList());
	// 	if (categoryString.size() == 2) {
	// 		this.monthlyCoffeeCategory.addCategories(categoryString);
	// 	} else {
	// 		this.monthlyCoffeeCategory.addCategory(categoryString.get(0));
	// 	}
	// 	this.coffeeDetailsTopTags = coffee.getCoffeeDetailsTopTags();
	// 	this.abv = coffee.getCoffeeDetailsBasic().getAbv();
	// 	this.ibu = coffee.getCoffeeDetailsBasic().getIbu();
	// 	this.averageStar = coffee.getCoffeeDetailsStars().getTotalAverageStars();
	// 	this.ratingCount = coffee.getCoffeeDetailsCounts().getRatingCount();
	// 	this.coffeeDetailsBestRating = coffee.getCoffeeDetailsBestRating();
	// }
}
