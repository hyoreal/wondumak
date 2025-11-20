package be.domain.coffeetag.entity;

public enum CoffeeTagType {

	/*
	 * ROASTING
	 */
	LIGHT_ROAST(),
	MEDIUM_ROAST(),
	DARK_ROAST(),

	/*
	 * TASTE
	 */
	SWEET(), // 단맛
	SOUR(), // 신맛 (Acidity)
	BITTER(), // 쓴맛
	BALANCED(), // 밸런스

	/*
	 * FLAVOR
	 */
	FRUITY(), // 과일향
	FLORAL(), // 꽃향
	NUTTY(), // 견과류
	CHOCOLATE(), // 초콜릿
	SPICY(), // 스파이시
	CARAMEL(), // 카라멜
	VANILLA(), // 바닐라
	SMOKY(), // 스모키

	/*
	 * BODY
	 */
	LIGHT_BODY(),
	MEDIUM_BODY(),
	HEAVY_BODY();
}
