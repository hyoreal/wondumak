package be.domain.coffee.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeetag.entity.CoffeeTag;
import be.global.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoffeeCoffeeTag extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coffee_coffee_tag_id")
	private Long id;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "coffee_id")
	private Coffee coffee;
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "coffee_tag_id")
	private CoffeeTag coffeeTag;

	public void addCoffee(Coffee coffee) {
		this.coffee = coffee;
		if (!this.coffee.getCoffeeCoffeeTags().contains(this)) {
			this.coffee.getCoffeeCoffeeTags().add(this);
		}
	}

	public void addCoffeeTag(CoffeeTag coffeeTag) {
		this.coffeeTag = coffeeTag;
		if (!this.coffeeTag.getCoffeeCoffeeTags().contains(this)) {
			this.coffeeTag.addCoffeeCoffeeTag(this);
		}
	}

	public void remove(Coffee coffee, CoffeeTag coffeeTag) {
		this.coffee = null;
		this.coffeeTag = null;
	}
}
