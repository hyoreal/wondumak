package be.domain.elasticsearch.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import be.domain.elasticsearch.dto.SearchResponseDto;
import be.domain.elasticsearch.entity.BeerDocument;

public interface BeerSearchRepositoryCustom {
	SearchResponseDto search(String keyword, Pageable pageable);
}
