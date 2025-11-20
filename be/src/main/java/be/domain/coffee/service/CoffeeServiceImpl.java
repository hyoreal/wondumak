package be.domain.coffee.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.entity.CoffeeCoffeeCategory;
import be.domain.coffee.entity.MonthlyCoffee;
import be.domain.coffee.entity.WeeklyCoffee;
import be.domain.coffee.repository.CoffeeCoffeeCategoryQueryRepository;
import be.domain.coffee.repository.CoffeeCoffeeCategoryRepository;
import be.domain.coffee.repository.CoffeeQueryRepository;
import be.domain.coffee.repository.CoffeeRepository;
import be.domain.coffee.repository.MonthlyCoffeeQueryRepository;
import be.domain.coffee.repository.MonthlyCoffeeRepository;
import be.domain.coffee.repository.WeeklyCoffeeQueryRepository;
import be.domain.coffee.repository.WeeklyCoffeeRepository;
import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeecategory.service.CoffeeCategoryService;
import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.rating.entity.Rating;
import be.domain.user.entity.User;
import be.domain.user.service.UserService;
import be.global.exception.BusinessLogicException;
import be.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeServiceImpl implements CoffeeService {

	private final UserService userService;
	private final CoffeeRepository coffeeRepository;
	private final CoffeeQueryRepository coffeeQueryRepository;
	private final WeeklyCoffeeRepository weeklyCoffeeRepository;
	private final WeeklyCoffeeQueryRepository weeklyCoffeeQueryRepository;
	private final MonthlyCoffeeRepository monthlyCoffeeRepository;
	private final MonthlyCoffeeQueryRepository monthlyCoffeeQueryRepository;
	private final CoffeeCategoryService coffeeCategoryService;
	private final CoffeeCoffeeCategoryRepository coffeeCoffeeCategoryRepository;
	private final CoffeeCoffeeCategoryQueryRepository coffeeCoffeeCategoryQueryRepository;

	@Override
	@Transactional
	public Coffee createCoffee(Coffee coffee) {

		Coffee savedCoffee = Coffee.builder()
			.coffeeWishlists(new ArrayList<>())
			.build();

		savedCoffee.create(coffee);
		saveCoffeeCoffeeCategories(savedCoffee, coffee);

		return coffeeRepository.save(savedCoffee);
	}

	@Override
	@Transactional
	public Coffee updateCoffee(Coffee coffee, Long coffeeId) {

		Coffee findCoffee = findVerifiedCoffee(coffeeId);

		findCoffee.update(coffee);

		deleteCoffeeCoffeeCategories(findCoffee);

		saveCoffeeCoffeeCategories(findCoffee, coffee);

		return coffeeRepository.save(findCoffee);
	}

	@Override
	@Transactional
	public void deleteCoffee(Long coffeeId) {

		coffeeRepository.deleteById(coffeeId);
	}

	//    public Coffee isWishListedCoffee(Coffee coffee, User user){
	//        return null;
	//    }

	//    public Comment isLikedComment(Comment comment, User user){
	//        return null;
	//    }

	//    public Coffee isLikedComments(Long coffeeId){
	//        return null;
	//    }

	@Override
	@Transactional
	public void createMonthlyCoffee() {

		List<MonthlyCoffee> monthlyCoffeesTemp = new ArrayList<>();
		List<Coffee> findCoffees = coffeeQueryRepository.findMonthlyCoffee();

		findCoffees.forEach(coffee -> {
			MonthlyCoffee monthlyCoffee = MonthlyCoffee.builder().build();

			monthlyCoffee.create(coffee);

			monthlyCoffeesTemp.add(monthlyCoffee);
		});

		monthlyCoffeeRepository.saveAll(monthlyCoffeesTemp);
	}

	@Override
	@Transactional
	public void createWeeklyCoffee() {

		List<WeeklyCoffee> weeklyCoffeesTemp = new ArrayList<>();
		List<Coffee> findCoffees = coffeeQueryRepository.findWeeklyCoffee();

		findCoffees.forEach(coffee -> {
			WeeklyCoffee weeklyCoffee = WeeklyCoffee.builder().build();

			weeklyCoffee.create(coffee);

			weeklyCoffeesTemp.add(weeklyCoffee);
		});

		weeklyCoffeeRepository.saveAll(weeklyCoffeesTemp);
	}

	@Override
	@Transactional
	public Coffee getCoffee(Long coffeeId) {

		return findVerifiedCoffee(coffeeId);
	}

	@Override
	// @Cacheable(MONTHLY_BEER)
	@Transactional(readOnly = true)
	public List<MonthlyCoffee> findMonthlyCoffees() {
		return monthlyCoffeeQueryRepository.findMonthlyCoffee();
	}

	@Override
	// @Cacheable(MONTHLY_BEER)
	@Transactional(readOnly = true)
	public List<WeeklyCoffee> findWeeklyCoffees() {
		return weeklyCoffeeQueryRepository.findWeeklyCoffee();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Coffee> findRecommendCoffees() {

		User findUser = userService.getLoginUserReturnNull();
		// try {
		// 	userService.getLoginUser();
		// } catch (BusinessLogicException e) {
		// 	return null;
		// }
		//
		// User findUser = userService.getLoginUser();
		if (findUser != null) {
			if (findUser.getUserCoffeeCategories().size() == 0) {
				return coffeeRepository.findRandomCoffee();
			} else {
				return coffeeQueryRepository.findRecommendCoffee(findUser);
			}
		} else {
			return coffeeRepository.findRandomCoffee();
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Rating findBestRating(Coffee coffee) {
		return coffeeQueryRepository.findBestRating(coffee);
	}

	@Override
	@Transactional(readOnly = true)
	public List<CoffeeTag> findTop4CoffeeTags(Coffee coffee) {
		return coffeeQueryRepository.findTop4CoffeeTag(coffee);
	}

	@Override
	@Transactional(readOnly = true)
	public String findBestPairingCategory(Coffee coffee) {
		return coffeeQueryRepository.findBestPairingCategory(coffee);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Coffee> findSimilarCoffees(Long coffeeId) {

		Coffee findCoffee = findVerifiedCoffee(coffeeId);
		return coffeeQueryRepository.findSimilarCoffee(findCoffee);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Rating> findMyRatingWithWishlist() {

		User loginUser = userService.getLoginUser();

		return coffeeQueryRepository.findMyRatingWithWishlist(loginUser);
	}

	@Override
	@Transactional(readOnly = true)
	public Coffee findCoffeeByRatingId(Long ratingId) {
		return coffeeQueryRepository.findCoffeeByRatingId(ratingId);
	}

	@Override
	@Transactional(readOnly = true)
	public Coffee findCoffeeByPairingId(Long pairingId) {
		return coffeeQueryRepository.findCoffeeByPairingId(pairingId);
	}

	@Override
	@Transactional(readOnly = true)
	public Coffee findVerifiedCoffee(Long coffeeId) {

		Optional<Coffee> optionalCoffee = coffeeRepository.findById(coffeeId);

		return optionalCoffee.orElseThrow(() ->
			new BusinessLogicException(ExceptionCode.BEER_NOT_FOUND));
	}

	private void saveCoffeeCoffeeCategories(Coffee savedCoffee, Coffee coffee) {

		coffee.getCoffeeCoffeeCategories()
			.forEach(coffeeCoffeeCategory -> {
				CoffeeCategory coffeeCategory =
					coffeeCategoryService.findVerifiedCoffeeCategory(
						coffeeCoffeeCategory.getCoffeeCategory().getCoffeeCategoryType());
				CoffeeCoffeeCategory savedCoffeeCoffeeCategory =
					CoffeeCoffeeCategory.builder()
						.coffee(savedCoffee)
						.coffeeCategory(coffeeCategory)
						.build();
				coffeeCoffeeCategoryRepository.save(savedCoffeeCoffeeCategory);
			});
	}

	private void deleteCoffeeCoffeeCategories(Coffee findCoffee) {
		findCoffee.getCoffeeCoffeeCategories().forEach(coffeeCoffeeCategory -> {
			coffeeCoffeeCategory.remove(findCoffee, coffeeCoffeeCategory.getCoffeeCategory());
			coffeeCoffeeCategoryQueryRepository.delete(coffeeCoffeeCategory);
		});
	}
}
