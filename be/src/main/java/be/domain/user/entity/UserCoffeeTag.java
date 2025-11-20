package be.domain.user.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import be.domain.coffeetag.entity.CoffeeTag;
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
public class UserCoffeeTag {

	@Id
	@Column(name = "user_coffee_tag_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	@ManyToOne
	@JoinColumn(name = "coffee_tag_id")
	private CoffeeTag coffeeTag;

	public void addUser(User user) {
		this.user = user;
		if (!this.user.getUserCoffeeTags().contains(this)) {
			this.user.getUserCoffeeTags().add(this);
		}
	}

	public void addCoffeeTag(CoffeeTag coffeeTag) {
		this.coffeeTag = coffeeTag;
		if (!this.user.getUserCoffeeTags().contains(this)) {
			this.user.getUserCoffeeTags().add(this);
		}
	}
}
