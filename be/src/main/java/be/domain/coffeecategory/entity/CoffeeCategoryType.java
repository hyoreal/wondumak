package be.domain.coffeecategory.entity;

public enum CoffeeCategoryType {
	ARABICA(),
	ROBUSTA(),
	LIBERICA(),
	EXCELSA(),
	BLEND(),
	DECAF(),
	COLD_BREW(),
	ESPRESSO_BASED();

	@Override
	public String toString() {
		return super.toString();
	}

	public Integer toInteger(CoffeeCategoryType coffeeCategoryType) {
		return coffeeCategoryType.ordinal();
	}
}
