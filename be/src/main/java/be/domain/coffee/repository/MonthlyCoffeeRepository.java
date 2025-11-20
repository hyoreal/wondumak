package be.domain.coffee.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.coffee.entity.MonthlyCoffee;

public interface MonthlyCoffeeRepository extends JpaRepository<MonthlyCoffee, Long> {
}
