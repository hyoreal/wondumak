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

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;

import be.domain.coffeecategory.entity.CoffeeCategory;
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
public class CoffeeCoffeeCategory extends BaseTimeEntity implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "coffee_coffee_category_id")
	private Long id;
	@JsonBackReference
	@JoinColumn(name = "coffee_id")
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@NotFound(action = NotFoundAction.IGNORE)
	private Coffee coffee;
	@JsonBackReference
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@JoinColumn(name = "coffee_category_id")
	private CoffeeCategory coffeeCategory;

	public void addCoffee(Coffee coffee) {
		this.coffee = coffee;
		if (!this.coffee.getCoffeeCoffeeCategories().contains(this)) {
			this.coffee.getCoffeeCoffeeCategories().add(this);
		}
	}

	public void addCoffeeCategory(CoffeeCategory coffeeCategory) {
		this.coffeeCategory = coffeeCategory;
		if (!this.coffeeCategory.getCoffeeCoffeeCategories().contains(this)) {
			this.coffeeCategory.addCoffeeCoffeeCategory(this);
		}
	}

	public void remove(Coffee coffee, CoffeeCategory coffeeCategory) {
		this.coffee = null;
		this.coffeeCategory = null;
	}
}
