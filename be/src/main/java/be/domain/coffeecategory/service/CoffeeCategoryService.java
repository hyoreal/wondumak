package be.domain.coffeecategory.service;

import org.springframework.stereotype.Service;

import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeecategory.entity.CoffeeCategoryType;
import be.domain.coffeecategory.repository.CoffeeCategoryRepository;
import be.global.exception.BusinessLogicException;
import be.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeCategoryService {
	private final CoffeeCategoryRepository coffeeCategoryRepository;

	public CoffeeCategory findVerifiedCoffeeCategory(CoffeeCategoryType coffeeCategoryType) {

		return coffeeCategoryRepository.findCoffeeCategoryByCoffeeCategoryType(coffeeCategoryType)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BEER_CATEGORY_NOT_FOUND));
	}

	public CoffeeCategory findVerifiedCoffeeCategoryById(Long coffeeCategoryId) {
		return coffeeCategoryRepository.findById(coffeeCategoryId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BEER_CATEGORY_NOT_FOUND));
	}
}
