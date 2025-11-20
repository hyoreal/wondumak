package be.domain.coffeetag.service;

import org.springframework.stereotype.Service;

import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeetag.entity.CoffeeTagType;
import be.domain.coffeetag.repository.CoffeeTagRepository;
import be.global.exception.BusinessLogicException;
import be.global.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CoffeeTagService {

	private final CoffeeTagRepository coffeeTagRepository;

	public CoffeeTag findVerifiedCoffeeTagByCoffeeTagType(CoffeeTagType coffeeTagType) {
		return coffeeTagRepository.findCoffeeTagByCoffeeTagType(coffeeTagType)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BEER_CATEGORY_NOT_FOUND));
	}

	public CoffeeTag findVerifiedCoffeeTag(Long coffeeTagId) {
		return coffeeTagRepository.findById(coffeeTagId)
			.orElseThrow(() -> new BusinessLogicException(ExceptionCode.BEER_CATEGORY_NOT_FOUND));
	}
}
