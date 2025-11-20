package be.global.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PageInfoWithCoffeeInfo {
	private int page;
	private int size;
	private long totalElements;
	private int totalPages;
	private Long coffeeId;
	private String coffeeKorName;
	private String coffeeEngName;
}
