package be.domain.elasticsearch.dto;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import be.domain.elasticsearch.entity.BeerDocument;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchResponseDto {
	private Page<BeerDocument> beerDocuments;
	private Map<String, List<Map<String, Object>>> aggregations;
}
