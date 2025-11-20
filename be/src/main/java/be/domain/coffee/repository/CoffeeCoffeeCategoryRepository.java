package be.domain.coffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.coffee.entity.CoffeeCoffeeCategory;

public interface CoffeeCoffeeCategoryRepository extends JpaRepository<CoffeeCoffeeCategory, Long> {
}
