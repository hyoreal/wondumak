package be.domain.elasticsearch.dto;

import javax.persistence.Embedded;

import be.domain.coffee.entity.CoffeeDetailsTopTags;
import lombok.Getter;

@Getter
public class SearchParam {

	private String korName;
	private String engName;
	private String country;
	private String category;
	@Embedded
	private CoffeeDetailsTopTags coffeeDetailsTopTags;
}
