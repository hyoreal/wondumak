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
public class WeeklyCoffee extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@Column(name = "weekly_coffee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long coffeeId;
	private String korName;
	private String country;
	private String thumbnail;
	@Embedded
	private WeeklyCoffeeCategory weeklyCoffeeCategory;
	private Double roasting; // Was abv
	private Integer acidity; // Was ibu
	private Double averageStar;

	public void create(Coffee coffee) {
		this.coffeeId = coffee.getId();
		this.korName = coffee.getCoffeeDetailsBasic().getKorName();
		this.country = coffee.getCoffeeDetailsBasic().getCountry();
		this.thumbnail = coffee.getCoffeeDetailsBasic().getThumbnail();
		List<String> categoryList = coffee.getCoffeeCoffeeCategories().stream()
			.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory().getCoffeeCategoryType().toString())
			.collect(Collectors.toList());
		if (categoryList.size() == 2) {
			this.weeklyCoffeeCategory.addCategories(categoryList);
		} else {
			this.weeklyCoffeeCategory.addCategory(categoryList.get(0));
		}
		this.roasting = coffee.getCoffeeDetailsBasic().getRoasting();
		this.acidity = coffee.getCoffeeDetailsBasic().getAcidity();
		this.averageStar = coffee.getCoffeeDetailsStars().getTotalAverageStars();
	}
}
