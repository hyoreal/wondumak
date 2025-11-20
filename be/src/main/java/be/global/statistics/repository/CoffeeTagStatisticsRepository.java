package be.global.statistics.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import be.global.statistics.entity.CoffeeTagStatistics;

public interface CoffeeTagStatisticsRepository extends JpaRepository<CoffeeTagStatistics, Long> {
}
