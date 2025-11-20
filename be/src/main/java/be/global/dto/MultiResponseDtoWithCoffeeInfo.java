package be.global.dto;

import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonInclude;

import be.domain.coffee.entity.Coffee;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultiResponseDtoWithCoffeeInfo<T> {
	private List<T> data;
	private PageInfoWithCoffeeInfo pageInfo;

	public MultiResponseDtoWithCoffeeInfo(List<T> data, Page page, Coffee coffee) {
		this.data = data;
		this.pageInfo = new PageInfoWithCoffeeInfo(page.getNumber() + 1,
			page.getSize(), page.getTotalElements(), page.getTotalPages(),
			coffee.getId(), coffee.getCoffeeDetailsBasic().getKorName(), coffee.getCoffeeDetailsBasic().getEngName());
	}
}
