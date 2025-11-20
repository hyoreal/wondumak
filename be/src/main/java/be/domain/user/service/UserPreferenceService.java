package be.domain.user.service;

import org.springframework.stereotype.Service;

import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeecategory.service.CoffeeCategoryService;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeetag.service.CoffeeTagService;
import be.domain.user.entity.User;
import be.domain.user.entity.UserCoffeeCategory;
import be.domain.user.entity.UserCoffeeTag;
import be.domain.user.repository.UserCoffeeCategoryQRepository;
import be.domain.user.repository.UserCoffeeCategoryRepository;
import be.domain.user.repository.UserCoffeeTagQRepository;
import be.domain.user.repository.UserCoffeeTagRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserPreferenceService {

	private final CoffeeTagService coffeeTagService;
	private final CoffeeCategoryService coffeeCategoryService;
	private final UserCoffeeTagRepository userCoffeeTagRepository;
	private final UserCoffeeTagQRepository userCoffeeTagQRepository;
	private final UserCoffeeCategoryRepository userCoffeeCategoryRepository;
	private final UserCoffeeCategoryQRepository userCoffeeCategoryQRepository;

	/* set UserCoffeeTags */
	protected void setUserCoffeeTags(User post, User user) {
		if (user.getUserCoffeeTags() != null) {
			userCoffeeTagQRepository.delete(user.getId());
		}

		post.getUserCoffeeTags().forEach(userCoffeeTag -> {
			CoffeeTag coffeeTag =
				coffeeTagService.findVerifiedCoffeeTagByCoffeeTagType(userCoffeeTag.getCoffeeTag().getCoffeeTagType());
			UserCoffeeTag saved = UserCoffeeTag.builder()
				.user(user)
				.coffeeTag(coffeeTag)
				.build();
			userCoffeeTagRepository.save(saved);
		});
	}

	/* set CoffeeCategories */
	protected void setUserCoffeeCategories(User post, User user) {
		if (user.getUserCoffeeCategories() != null) {
			userCoffeeCategoryQRepository.delete(user.getId());
		}

		post.getUserCoffeeCategories().forEach(userCoffeeCategory -> {
			CoffeeCategory coffeeCategory =
				coffeeCategoryService.findVerifiedCoffeeCategory(userCoffeeCategory.getCoffeeCategory().getCoffeeCategoryType());
			UserCoffeeCategory saved = UserCoffeeCategory.builder()
				.user(user)
				.coffeeCategory(coffeeCategory)
				.build();
			userCoffeeCategoryRepository.save(saved);
		});
	}
}
