package be.global.aop;

import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class Pointcuts {

	@Pointcut("execution(* be.domain.coffee.controller.CoffeeController.getWeeklyCoffee())")
	public void getWeeklyCoffee() {
	}

	@Pointcut("execution(* be.domain.coffee.service.CoffeeServiceImpl.getCoffee(..))")
	public void getCoffee() {
	}

	@Pointcut("execution(* be.domain.rating.service.RatingService.create(..))")
	public void createRating() {
	}

	@Pointcut("execution(* be.domain.rating.service.RatingService.update(..))")
	public void updateRating() {
	}

	@Pointcut("execution(* be.domain.rating.service.RatingService.delete(..))")
	public void deleteRating() {
	}

	@Pointcut("execution(* be.domain.pairing.service.PairingService.create(..))")
	public void createPairing() {
	}

	@Pointcut("execution(* be.domain.pairing.service.PairingService.update(..))")
	public void updatePairing() {
	}

	@Pointcut("execution(* be.domain.pairing.service.PairingService.delete(..))")
	public void deletePairing() {
	}

	@Pointcut("execution(* be.domain.like.controller.RatingLikeController.clickLike(..))")
	public void clickRatingLike() {
	}

	@Pointcut("execution(* be.domain.user.service.UserService.updateUser(..))")
	public void updateUser() {
	}
}
