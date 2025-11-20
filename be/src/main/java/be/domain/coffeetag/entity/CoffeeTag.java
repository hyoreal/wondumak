package be.domain.coffeetag.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import be.domain.coffee.entity.CoffeeCoffeeTag;
import be.domain.user.entity.UserCoffeeTag;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@DynamicInsert
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoffeeTag implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@Column(name = "coffee_tag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private CoffeeTagType coffeeTagType;
	@ColumnDefault("0")
	private Integer statCount;

	@OneToMany(mappedBy = "coffeeTag")
	private List<CoffeeCoffeeTag> coffeeCoffeeTags = new ArrayList<>();

	@OneToMany(mappedBy = "coffeeTag")
	private List<UserCoffeeTag> userCoffeeTags = new ArrayList<>();

	public void addCoffeeCoffeeTag(CoffeeCoffeeTag coffeeCoffeeTag) {
		this.coffeeCoffeeTags.add(coffeeCoffeeTag);
		if (coffeeCoffeeTag.getCoffeeTag() != this) {
			coffeeCoffeeTag.addCoffeeTag(this);
		}
	}

	public void addDailyCount() {
		this.statCount++;
	}

	public void subtractDailyCount() {
		if (this.statCount != 0) {
			this.statCount--;
		} else {
			this.statCount = 0;
		}
	}

	public void resetStatCount() {
		this.statCount = 0;
	}

}
