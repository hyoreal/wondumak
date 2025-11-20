package be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.user.entity.UserCoffeeCategory;

public interface UserCoffeeCategoryRepository extends JpaRepository<UserCoffeeCategory, Long> {
}
