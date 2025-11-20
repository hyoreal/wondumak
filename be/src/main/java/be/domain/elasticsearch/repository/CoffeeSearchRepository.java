package be.domain.elasticsearch.repository;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import be.domain.elasticsearch.entity.CoffeeDocument;

public interface CoffeeSearchRepository extends ElasticsearchRepository<CoffeeDocument, Long> {

	List<CoffeeDocument> findByKorName(String korName);
}
