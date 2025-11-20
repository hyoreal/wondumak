package be.domain.coffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.coffee.entity.WeeklyCoffee;

public interface WeeklyCoffeeRepository extends JpaRepository<WeeklyCoffee, Long> {
}
