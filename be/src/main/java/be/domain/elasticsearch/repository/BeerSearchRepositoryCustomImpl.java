package be.domain.elasticsearch.repository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import be.domain.elasticsearch.dto.SearchResponseDto;
import be.domain.elasticsearch.entity.BeerDocument;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class BeerSearchRepositoryCustomImpl implements BeerSearchRepositoryCustom {

	private final ElasticsearchRestTemplate elasticsearchRestTemplate;

	@Override
	public SearchResponseDto search(String keyword, Pageable pageable) {

		NativeSearchQuery query = new NativeSearchQueryBuilder()
			.withQuery(
				QueryBuilders.multiMatchQuery(keyword, "korName", "engName")
			)
			.withAggregation(
				"beerCategory", AggregationBuilders.terms("beerCategory").field("beerCategory")
			)
			.withAggregation(
				"country", AggregationBuilders.terms("country").field("country")
			)
			.withPageable(pageable)
			.build();

		SearchHits<BeerDocument> searchHits = elasticsearchRestTemplate.search(query, BeerDocument.class);
		List<BeerDocument> beerDocuments = searchHits.getSearchHits().stream()
			.map(SearchHit::getContent)
			.collect(Collectors.toList());

		Page<BeerDocument> page = new PageImpl<>(beerDocuments, pageable, searchHits.getTotalHits());

		Map<String, List<Map<String, Object>>> aggregations = searchHits.getAggregations().asMap().entrySet().stream()
			.collect(Collectors.toMap(
				Map.Entry::getKey,
				entry -> ((Terms)entry.getValue()).getBuckets().stream()
					.map(bucket -> Map.of(
						"key", bucket.getKeyAsString(),
						"count", bucket.getDocCount()
					))
					.collect(Collectors.toList())
			));

		return SearchResponseDto.builder()
			.beerDocuments(page)
			.aggregations(aggregations)
			.build();
	}
}
