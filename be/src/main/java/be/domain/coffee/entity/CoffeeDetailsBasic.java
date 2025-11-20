package be.domain.coffee.entity;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Embeddable
@RequiredArgsConstructor
public class CoffeeDetailsBasic {

	@NotBlank
	private String korName;
	@NotBlank
	private String engName;
	@NotBlank
	private String country;
	@NotBlank
	private String thumbnail;
	@NotBlank
	private Double roasting; // Was abv
	private Integer acidity; // Was ibu

	@Builder
	public CoffeeDetailsBasic(String korName, String engName, String country,
		String thumbnail, Double roasting, Integer acidity) {
		this.korName = korName;
		this.engName = engName;
		this.country = country;
		this.thumbnail = thumbnail;
		this.roasting = roasting;
		this.acidity = acidity;
	}
}
