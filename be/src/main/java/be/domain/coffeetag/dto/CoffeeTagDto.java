package be.domain.coffeetag.dto;

import be.domain.coffeetag.entity.CoffeeTagType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class CoffeeTagDto {

	@Getter
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class Response {

		private Long coffeeTagId;
		private CoffeeTagType coffeeTagType;
	}
}
