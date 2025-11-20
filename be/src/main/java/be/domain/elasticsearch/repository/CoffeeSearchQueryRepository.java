package be.domain.elasticsearch.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Component;

import be.domain.elasticsearch.entity.CoffeeDocument;
import be.domain.elasticsearch.dto.SearchParam;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CoffeeSearchQueryRepository {
	private final ElasticsearchOperations elasticsearchOperations;

	public List<CoffeeDocument> findByCondition(SearchParam searchParam, Pageable pageable) {
		return null;
	}

	public List<CoffeeDocument> findByName(String name, Pageable pageable) {
		CriteriaQuery criteriaQuery = new CriteriaQuery(new Criteria());

		criteriaQuery.addCriteria(Criteria.where("korName").is(name)
			.or(Criteria.where("engName").is(name))).setPageable(pageable);

		SearchHits<CoffeeDocument> searchHits = elasticsearchOperations.search(criteriaQuery, CoffeeDocument.class);

		return searchHits.stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());
	}
}
