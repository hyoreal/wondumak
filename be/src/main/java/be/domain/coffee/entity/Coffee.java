package be.domain.coffee.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.DynamicInsert;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import be.domain.coffeewishlist.entity.CoffeeWishlist;
import be.domain.pairing.entity.Pairing;
import be.domain.rating.entity.Rating;
import be.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Entity
@Getter
@Builder
@ToString
@DynamicInsert
// @Document(indexName = "coffees")
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Coffee extends BaseTimeEntity implements Serializable {

	private static final Long serialVersionUID = 6494678977089006639L;

	@Id
	@Column(name = "coffee_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Embedded
	private CoffeeDetailsBasic coffeeDetailsBasic;
	@Embedded
	private CoffeeDetailsStars coffeeDetailsStars;
	@Embedded
	private CoffeeDetailsCounts coffeeDetailsCounts;
	@Embedded
	private CoffeeDetailsTopTags coffeeDetailsTopTags;
	@Embedded
	@Nullable
	private CoffeeDetailsBestRating coffeeDetailsBestRating;
	@Embedded
	@Nullable
	private CoffeeDetailsStatistics coffeeDetailsStatistics;

	// @PersistenceConstructor
	public Coffee(Long id, CoffeeDetailsBasic coffeeDetailsBasic, CoffeeDetailsStars coffeeDetailsStars,
		CoffeeDetailsCounts coffeeDetailsCounts, CoffeeDetailsTopTags coffeeDetailsTopTags,
		@Nullable CoffeeDetailsBestRating coffeeDetailsBestRating, @Nullable CoffeeDetailsStatistics coffeeDetailsStatistics) {
		this.id = id;
		this.coffeeDetailsBasic = coffeeDetailsBasic;
		this.coffeeDetailsStars = coffeeDetailsStars;
		this.coffeeDetailsCounts = coffeeDetailsCounts;
		this.coffeeDetailsTopTags = coffeeDetailsTopTags;
		this.coffeeDetailsBestRating = coffeeDetailsBestRating;
		this.coffeeDetailsStatistics = coffeeDetailsStatistics;
	}

	@JsonManagedReference
	@OneToMany(mappedBy = "coffee", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<CoffeeCoffeeCategory> coffeeCoffeeCategories = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy = "coffee", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<CoffeeCoffeeTag> coffeeCoffeeTags = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy = "coffee", cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, orphanRemoval = true)
	private List<CoffeeWishlist> coffeeWishlists = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy = "coffee", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Rating> ratingList = new ArrayList<>();
	@JsonManagedReference
	@OneToMany(mappedBy = "coffee", cascade = CascadeType.PERSIST, orphanRemoval = true)
	private List<Pairing> pairingList = new ArrayList<>();

	public void addCoffeeCoffeeCategories(List<CoffeeCoffeeCategory> coffeeCoffeeCategories) {
		this.coffeeCoffeeCategories = coffeeCoffeeCategories;
	}

	/*
	 * STUB DATA 생성을 위한 메서드
	 */
	public void addCoffeeDetailsCounts(CoffeeDetailsCounts coffeeDetailsCounts) {
		this.coffeeDetailsCounts = coffeeDetailsCounts;
	}

	public void addCoffeeDetailsStars(CoffeeDetailsStars coffeeDetailsStars) {
		this.coffeeDetailsStars = coffeeDetailsStars;
	}

	//    ------------------------------------------------------------------------

	public void addCoffeeCoffeeCategory(CoffeeCoffeeCategory coffeeCoffeeCategory) {
		this.coffeeCoffeeCategories.add(coffeeCoffeeCategory);
		if (coffeeCoffeeCategory.getCoffee() != this) {
			coffeeCoffeeCategory.addCoffee(this);
		}
	}

	public void addCoffeeCoffeeTag(CoffeeCoffeeTag coffeeCoffeeTag) {
		this.coffeeCoffeeTags.add(coffeeCoffeeTag);

		if (coffeeCoffeeTag.getCoffee() != this) {
			coffeeCoffeeTag.addCoffee(this);
		}
	}

	public void addCoffeeWishlists(CoffeeWishlist coffeeWishlist) {
		this.coffeeWishlists.add(coffeeWishlist);

		if (coffeeWishlist.getCoffee() != this) {
			coffeeWishlist.addCoffee(this);
		}
	}

	public void addPairingList(Pairing pairing) {
		pairingList.add(pairing);

		if (pairing.getCoffee() != this) {
			pairing.belongToCoffee(this);
		}
	}

	public void addRatingList(Rating rating) {
		ratingList.add(rating);

		if (rating.getCoffee() != this) {
			rating.belongToCoffee(this);
		}
	}

	public void makeTopTagsNull() {
		this.coffeeDetailsTopTags = null;
	}

	public void create(Coffee coffee) {
		this.coffeeDetailsBasic = coffee.getCoffeeDetailsBasic();
	}

	public void createTopTags(List<String> coffeeTagTypes) {

		this.coffeeDetailsTopTags =
			CoffeeDetailsTopTags.builder()
				.tag1(coffeeTagTypes.get(0))
				.tag2(coffeeTagTypes.get(1))
				.tag3(coffeeTagTypes.get(2))
				.tag4(coffeeTagTypes.get(3))
				.build();
	}

	public List<String> createTopTagList() {
		return List.of(this.coffeeDetailsTopTags.getTag1(),
			this.coffeeDetailsTopTags.getTag2(),
			this.coffeeDetailsTopTags.getTag3(),
			this.coffeeDetailsTopTags.getTag4()
		);
	}

	public void updateBestRating(Rating rating) {
		this.coffeeDetailsBestRating =
			CoffeeDetailsBestRating.builder()
				.bestRatingId(rating.getId())
				.bestUserId(rating.getUser().getId())
				.bestStar(rating.getStar())
				.profileImage(rating.getUser().getImageUrl())
				.bestNickname(rating.getUser().getNickname())
				.bestContent(rating.getContent())
				.bestLikeCount(rating.getLikeCount())
				.build();
	}

	public void update(Coffee coffee) {
		this.coffeeDetailsBasic = coffee.getCoffeeDetailsBasic();
	}

	public void addStatViewCount() {
		this.coffeeDetailsStatistics.addStatViewCount();
	}

	public void addStatRatingCount() {
		this.coffeeDetailsStatistics.addStatRatingCount();
	}

	public void addRatingCount() {
		this.coffeeDetailsCounts.addRatingCount();
	}

	public void addFemaleStarCount() {
		this.coffeeDetailsCounts.addFemaleStarCount();
	}

	public void addMaleStarCount() {
		this.coffeeDetailsCounts.addMaleStarCount();
	}

	public void minusRatingCount() {
		this.coffeeDetailsCounts.minusRatingCount();
	}

	public void minusFemaleStarCount() {
		this.coffeeDetailsCounts.minusFemaleStarCount();
	}

	public void minusMaleStarCount() {
		this.coffeeDetailsCounts.minusMaleStarCount();
	}

	public void calculateTotalAverageStars(Double star) {
		this.coffeeDetailsStars.calculateTotalAverageStars(star, this.coffeeDetailsCounts.getRatingCount());
		this.coffeeDetailsCounts.addRatingCount();
	}

	public void calculateFemaleAverageStars(Double star) {
		this.coffeeDetailsStars.calculateFemaleAverageStars(star, this.coffeeDetailsCounts.getFemaleStarCount());
		this.coffeeDetailsCounts.addFemaleStarCount();
	}

	public void calculateMaleAverageStars(Double star) {
		this.coffeeDetailsStars.calculateMaleAverageStars(star, this.coffeeDetailsCounts.getMaleStarCount());
		this.coffeeDetailsCounts.addMaleStarCount();
	}

	public void updateTotalAverageStars(Double previousStar, Double afterStar) {
		this.coffeeDetailsStars.updateTotalAverageStars(previousStar, afterStar, this.coffeeDetailsCounts.getRatingCount());
	}

	public void updateFemaleAverageStars(Double previousStar, Double afterStar) {
		this.coffeeDetailsStars.updateFemaleAverageStars(previousStar, afterStar,
			this.coffeeDetailsCounts.getFemaleStarCount());
	}

	public void updateMaleAverageStars(Double previousStar, Double afterStar) {
		this.coffeeDetailsStars.updateMaleAverageStars(previousStar, afterStar,
			this.coffeeDetailsCounts.getMaleStarCount());
	}

	public void deleteTotalAverageStars(Double deleteStar) {
		this.coffeeDetailsStars.deleteTotalAverageStars(deleteStar, this.coffeeDetailsCounts.getRatingCount());
	}

	public void deleteFemaleAverageStars(Double deleteStar) {
		this.coffeeDetailsStars.deleteFemaleAverageStars(deleteStar, this.coffeeDetailsCounts.getFemaleStarCount());
		this.coffeeDetailsCounts.minusFemaleStarCount();
	}

	public void deleteMaleAverageStars(Double deleteStar) {
		this.coffeeDetailsStars.deleteMaleAverageStars(deleteStar, this.coffeeDetailsCounts.getMaleStarCount());
		this.coffeeDetailsCounts.minusMaleStarCount();
	}

	public void correct(
		Integer totalStarCount, Integer totalFemaleStarCount, Integer totalMaleStarCount) {

		this.coffeeDetailsCounts.setRatingCount(totalStarCount);
		this.coffeeDetailsCounts.setFemaleStarCount(totalFemaleStarCount);
		this.coffeeDetailsCounts.setMaleStarCount(totalMaleStarCount);
	}

	public void correct(
		Integer totalStarCount, Double totalAverageStars,
		Integer totalFemaleStarCount, Double totalFemaleAverageStars,
		Integer totalMaleStarCount, Double totalMaleAverageStars) {

		this.coffeeDetailsCounts.setRatingCount(totalStarCount);
		this.coffeeDetailsCounts.setFemaleStarCount(totalFemaleStarCount);
		this.coffeeDetailsCounts.setMaleStarCount(totalMaleStarCount);

		this.coffeeDetailsStars.setTotalAverageStars(totalAverageStars);
		this.coffeeDetailsStars.setFemaleAverageStars(totalFemaleAverageStars);
		this.coffeeDetailsStars.setMaleAverageStars(totalMaleAverageStars);
	}

	public void deleteCoffeeDetailsBestRating() {
		this.coffeeDetailsBestRating = null;
	}

}
