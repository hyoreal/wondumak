package be.domain.coffeetag.repository;

import org.springframework.stereotype.Repository;

import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class CoffeeTagQueryRepository {
	private final JPAQueryFactory jpaQueryFactory;

}
