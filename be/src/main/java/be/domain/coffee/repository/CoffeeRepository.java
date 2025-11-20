package be.domain.coffee.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import be.domain.coffee.entity.Coffee;

public interface CoffeeRepository extends JpaRepository<Coffee, Long> {

	@Query(nativeQuery = true, value = "select * " +
		"from coffee " +
		"order by " + "rand() " +
		"limit 5")
	List<Coffee> findRandomCoffee();
}
