package be.domain.rating.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import be.domain.coffeetag.entity.CoffeeTagType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Entity
public class RatingTag {

	protected RatingTag() {
	}

	@Id
	@Column(name = "rating_tag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Enumerated(EnumType.STRING)
	private CoffeeTagType color;

	@Enumerated(EnumType.STRING)
	private CoffeeTagType taste;

	@Enumerated(EnumType.STRING)
	private CoffeeTagType flavor;

	@Enumerated(EnumType.STRING)
	private CoffeeTagType carbonation;

	@OneToOne(mappedBy = "ratingTag", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private Rating rating;

	public void oneToOneByRating(Rating rating) {
		this.rating = rating;

		if (rating.getRatingTag() != this) {
			rating.oneToOneByRatingTag(this);
		}
	}

	@Builder
	public RatingTag(Long id, CoffeeTagType color, CoffeeTagType taste, CoffeeTagType flavor, CoffeeTagType carbonation,
		Rating rating) {
		this.id = id;
		this.color = color;
		this.taste = taste;
		this.flavor = flavor;
		this.carbonation = carbonation;
		this.rating = rating;
	}

	public void updateRatingTag(RatingTag ratingTag) {
		this.color = ratingTag.getColor();
		this.taste = ratingTag.getTaste();
		this.flavor = ratingTag.getFlavor();
		this.carbonation = ratingTag.getCarbonation();
	}

	public void saveRating(Rating rating) {
		this.rating = rating;
	}

	public List<CoffeeTagType> createCoffeeTagTypeList() {
		return List.of(color, flavor, taste, carbonation);
	}
}
