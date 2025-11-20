package be.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.domain.user.entity.UserCoffeeTag;

public interface UserCoffeeTagRepository extends JpaRepository<UserCoffeeTag, Long> {
}
