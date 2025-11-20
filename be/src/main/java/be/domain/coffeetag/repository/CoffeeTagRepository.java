package be.domain.coffeetag.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.coffeetag.entity.CoffeeTag;
import be.domain.coffeetag.entity.CoffeeTagType;

public interface CoffeeTagRepository extends JpaRepository<CoffeeTag, Long> {

	Optional<CoffeeTag> findCoffeeTagByCoffeeTagType(CoffeeTagType coffeeTagType);
}
