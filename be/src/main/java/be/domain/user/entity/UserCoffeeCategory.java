package be.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import be.domain.coffeecategory.entity.CoffeeCategory;
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
public class UserCoffeeCategory {

	@Id
	@Column(name = "user_coffee_category_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "coffee_category_id")
	private CoffeeCategory coffeeCategory;

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getUserCoffeeCategories().contains(this)) {
			this.user.getUserCoffeeCategories().add(this);
		}
	}

	public void addCoffeeCategory(CoffeeCategory coffeeCategory) {
		this.coffeeCategory = coffeeCategory;
		if (!this.user.getUserCoffeeCategories().contains(this)) {
			this.coffeeCategory.addUserCoffeeCategory(this);
		}
	}
}
