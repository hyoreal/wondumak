package be.global.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.global.statistics.entity.CoffeeCategoryStatistics;

public interface CoffeeCategoryStatisticsRepository extends JpaRepository<CoffeeCategoryStatistics, Long> {
}
