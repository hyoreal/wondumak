package be.domain.coffeewishlist.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonBackReference;

import be.domain.coffee.entity.Coffee;
import be.domain.user.entity.User;
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
// @ToString
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CoffeeWishlist extends BaseTimeEntity {

	@Id
	@Column(name = "coffee_wishlist_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private Boolean wished;

	@JsonBackReference
	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "coffee_id")
	private Coffee coffee;

	@ManyToOne(cascade = CascadeType.PERSIST)
	@JoinColumn(name = "user_id")
	private User user;

	public void addCoffee(Coffee coffee) {
		this.coffee = coffee;
		if (!this.coffee.getCoffeeWishlists().contains(this)) {
			this.coffee.getCoffeeWishlists().add(this);
		}
	}

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getCoffeeWishlists().contains(this)) {
			this.user.getCoffeeWishlists().add(this);
		}
	}
}
