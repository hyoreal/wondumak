package be.domain.coffeewishlist.service;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.service.CoffeeService;
import be.domain.coffeewishlist.entity.CoffeeWishlist;
import be.domain.coffeewishlist.repository.CoffeeWishListQRepository;
import be.domain.coffeewishlist.repository.CoffeeWishlistRepository;
import be.domain.coffeewishlist.service.pattern.WishButton;
import be.domain.user.entity.User;
import be.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class CoffeeWishlistServiceImpl implements CoffeeWishlistService {
	private final EntityManager em;
	private final UserService userService;
	private final CoffeeService coffeeService;
	private final CoffeeWishlistRepository coffeeWishlistRepository;
	private final CoffeeWishListQRepository coffeeWishListQRepository;
	private static WishButton wishButton = new WishButton();

	@Override
	public void createWishlist(Coffee coffee, User user) {

		CoffeeWishlist saved = CoffeeWishlist.builder()
			.coffee(coffee)
			.user(user)
			.wished(true)
			.build();

		coffeeWishlistRepository.save(saved);
	}

	@Override
	public void deleteWishlist(Long coffeeId) {
		coffeeWishListQRepository.deleteByCoffee(coffeeId);
	}

	@Override
	public CoffeeWishlist getIsWishlist(Coffee coffee) {

		User user = null;
		try {
			user = userService.getLoginUser();
		} catch (Exception e) {
		}

		return user == null ? null : coffeeWishlistRepository.findByCoffeeAndUser(coffee, user);
	}

	@Override
	public Page<CoffeeWishlist> getUserWishlist(Integer page, Integer size) {

		User loginUser = userService.getLoginUser();
		PageRequest pageRequest = PageRequest.of(page - 1, size);
		return coffeeWishListQRepository.findByUserAndTrue(loginUser.getId(), pageRequest);
	}

	@Override
	@Transactional
	public void wishStatePattern(Coffee coffee, User user) {
		wishButton.clickButton(wishButton, coffeeWishlistRepository, user, coffee);
	}

	@Override
	@Transactional
	public void verifyWishState(Long coffeeId) {
		Coffee coffee = coffeeService.getCoffee(coffeeId);
		User user = userService.getLoginUser();

		if (coffeeWishlistRepository.findByCoffeeAndUser(coffee, user) == null) {
			createWishlist(coffee, user);
		} else {
			wishStatePattern(coffee, user);
		}
	}
}
