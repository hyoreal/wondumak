package be.domain.elasticsearch.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Service;

import be.domain.coffee.repository.CoffeeRepository;
import be.domain.elasticsearch.entity.CoffeeDocument;
import be.domain.elasticsearch.repository.CoffeeSearchQueryRepository;
import be.domain.elasticsearch.repository.CoffeeSearchRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ElasticsearchService {
	private final CoffeeRepository coffeeRepository;
	private final CoffeeSearchRepository coffeeSearchRepository;
	private final ElasticsearchOperations elasticsearchOperations;
	private final CoffeeSearchQueryRepository coffeeSearchQueryRepository;

	/*
	 * 인덱스 생성 메서드
	 */
	public String createIndex() {

		IndexCoordinates indexCoordinates = elasticsearchOperations.getIndexCoordinatesFor(CoffeeDocument.class);

		IndexQuery indexQuery = new IndexQueryBuilder()
			.withId(UUID.randomUUID().toString())
			.withObject(CoffeeDocument.builder().build())
			.build();

		return elasticsearchOperations.index(indexQuery, indexCoordinates);
	}

	/*
	 * Document 삽입 메서드
	 */
	public List<CoffeeDocument> insertDocument(List<CoffeeDocument> analysisCoffeeList) {
		return (List<CoffeeDocument>)elasticsearchOperations.save(analysisCoffeeList);
	}

	/*
	 * DB -> ES 데이터 이관 메서드
	 */
	public void saveAllCoffeeDocuments() {

		List<CoffeeDocument> coffeeDocumentList =
			coffeeRepository.findAll().stream()
				.map(CoffeeDocument::toEntity)
				.collect(Collectors.toList());

		coffeeSearchRepository.saveAll(coffeeDocumentList);
	}

	public List<CoffeeDocument> searchByName(String name) {

		PageRequest pageRequest = PageRequest.of(0, 10);

		return coffeeSearchQueryRepository.findByName(name, pageRequest);
	}
}
