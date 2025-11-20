package be.global.statistics.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.WeekFields;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.data.annotation.CreatedDate;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.CoffeeDetailsStars;
import be.domain.coffee.entity.CoffeeDetailsTopTags;
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
public class CoffeeStatistics {
	@Id
	@Column(name = "coffee_statistics_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@CreatedDate
	private LocalDateTime createdAt;
	private LocalDate date;
	private Integer week;
	private Long coffeeId;
	private String korName;
	private String category1;
	private String category2;
	private CoffeeDetailsStars coffeeDetailsStars;
	private CoffeeDetailsTopTags coffeeDetailsTopTags;
	private Integer viewCount;
	private Integer ratingCount;

	public void create(Coffee coffee) {
		this.createdAt = LocalDateTime.now();
		this.date = LocalDate.now().minusDays(1);
		this.week = LocalDate.now().get(WeekFields.ISO.weekOfYear());
		this.coffeeId = coffee.getId();
		this.korName = coffee.getCoffeeDetailsBasic().getKorName();

		List<String> list = coffee.getCoffeeCoffeeCategories().stream()
			.map(coffeeCoffeeCategory -> coffeeCoffeeCategory.getCoffeeCategory().toString())
			.collect(Collectors.toList());
		if (list.size() == 1) {
			this.category1 = list.get(0);
			this.category2 = "";
		} else {
			this.category1 = list.get(0);
			this.category2 = list.get(1);
		}
		this.coffeeDetailsStars = coffee.getCoffeeDetailsStars();
		if (coffee.getCoffeeDetailsTopTags() == null) {
			this.coffeeDetailsTopTags =
				CoffeeDetailsTopTags.builder()
					.tag1(null)
					.tag2(null)
					.tag3(null)
					.tag4(null)
					.build();
		} else {
			this.coffeeDetailsTopTags = coffee.getCoffeeDetailsTopTags();
		}
		this.viewCount = coffee.getCoffeeDetailsStatistics().getStatViewCount();
		this.ratingCount = coffee.getCoffeeDetailsStatistics().getStatRatingCount();
		this.createdAt = LocalDateTime.now();
	}
}
