package be.domain.coffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.coffee.entity.CoffeeCoffeeTag;

public interface CoffeeCoffeeTagRepository extends JpaRepository<CoffeeCoffeeTag, Long> {
}
