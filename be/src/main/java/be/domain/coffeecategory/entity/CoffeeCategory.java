package be.domain.coffeecategory.entity;

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

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.querydsl.core.annotations.QueryProjection;

import be.domain.coffee.entity.CoffeeCoffeeCategory;
import be.domain.user.entity.UserCoffeeCategory;
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
public class CoffeeCategory implements Serializable {

	private static final long serialVersionUID = 6494678977089006639L;

	@Id
	@Column(name = "coffee_category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Enumerated(EnumType.STRING)
	private CoffeeCategoryType coffeeCategoryType;
	@ColumnDefault("0")
	private Integer statCount;

	@JsonManagedReference
	@OneToMany(mappedBy = "coffeeCategory")
	private List<CoffeeCoffeeCategory> coffeeCoffeeCategories = new ArrayList<>();

	@JsonManagedReference
	@OneToMany(mappedBy = "coffeeCategory")
	private List<UserCoffeeCategory> userCoffeeCategories = new ArrayList<>();

	public void addCoffeeCoffeeCategory(CoffeeCoffeeCategory coffeeCoffeeCategory) {
		this.coffeeCoffeeCategories.add(coffeeCoffeeCategory);
		if (coffeeCoffeeCategory.getCoffeeCategory() != this) {
			coffeeCoffeeCategory.addCoffeeCategory(this);
		}
	}

	public void addUserCoffeeCategory(UserCoffeeCategory userCoffeeCategory) {
		this.userCoffeeCategories.add(userCoffeeCategory);
		if (userCoffeeCategory.getCoffeeCategory() != this) {
			userCoffeeCategory.addCoffeeCategory(this);
		}
	}

	@QueryProjection
	public CoffeeCategory(Long id, CoffeeCategoryType coffeeCategoryType) {
		this.id = id;
		this.coffeeCategoryType = coffeeCategoryType;
	}

	public void addStatCount() {
		this.statCount++;
	}

	public void resetStatCount() {
		this.statCount = 0;
	}
}
