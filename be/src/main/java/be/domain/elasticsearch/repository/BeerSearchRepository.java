package be.domain.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import be.domain.elasticsearch.entity.BeerDocument;

public interface BeerSearchRepository extends ElasticsearchRepository<BeerDocument, Long>, BeerSearchRepositoryCustom {
}
