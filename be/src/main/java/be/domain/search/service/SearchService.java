package be.domain.search.service;

import java.util.ArrayList;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import be.domain.coffee.entity.Coffee;
import be.domain.coffee.repository.CoffeeQueryRepository;
import be.domain.search.repository.SearchQueryRepository;
import be.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class SearchService {
	private final CoffeeQueryRepository coffeeQueryRepository;
	private final SearchQueryRepository searchQueryRepository;

	@Transactional(readOnly = true)
	public Page<Coffee> findCoffeesPageByQueryParam(String queryParam, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		Page<Coffee> coffeePage;

		if (queryParam.isEmpty()) {
			coffeePage = new PageImpl<>(new ArrayList<>());
		} else if (queryParam.charAt(0) == '*') {
			coffeePage = searchQueryRepository.findCoffeesPageByCoffeeCategoryQueryParam(queryParam, pageRequest);
		} else if (queryParam.charAt(0) == '#') {
			coffeePage = searchQueryRepository.findCoffeesPageByCoffeeTagQueryParam(queryParam, pageRequest);
		} else if (queryParam.charAt(0) == '&') {
			coffeePage = searchQueryRepository.findCoffeesPageByPairingCategoryQueryParam(queryParam, pageRequest);
		} else {
			coffeePage = searchQueryRepository.findCoffeesPageByQueryParam(queryParam, pageRequest);
		}

		return coffeePage;
	}

	@Transactional(readOnly = true)
	public Page<User> findUsersPageByQueryParam(User findUser, String queryParam, Integer page) {

		PageRequest pageRequest = PageRequest.of(page - 1, 10);

		return searchQueryRepository.findUsersPageByQueryParam(findUser, queryParam, pageRequest);
	}
}
