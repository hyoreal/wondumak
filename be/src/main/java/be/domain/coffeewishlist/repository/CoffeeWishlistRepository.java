package be.domain.coffeewishlist.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.coffee.entity.Coffee;
import be.domain.coffeewishlist.entity.CoffeeWishlist;
import be.domain.user.entity.User;

public interface CoffeeWishlistRepository extends JpaRepository<CoffeeWishlist, Long> {

	CoffeeWishlist findByCoffee(Coffee coffee);

	CoffeeWishlist findByCoffeeAndUser(Coffee coffee, User user);
}
