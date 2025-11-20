package be.domain.coffeecategory.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.coffeecategory.entity.CoffeeCategory;
import be.domain.coffeecategory.entity.CoffeeCategoryType;

public interface CoffeeCategoryRepository extends JpaRepository<CoffeeCategory, Long> {

	Optional<CoffeeCategory> findCoffeeCategoryByCoffeeCategoryType(CoffeeCategoryType coffeeCategoryType);
}
