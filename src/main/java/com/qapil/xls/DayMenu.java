package com.qapil.xls;

import java.time.LocalDate;
import java.util.Iterator;

class DayMenu {

	private final RowIterator rowIterator;
	private final MealParser mealParser;
	private final LocalDate date;

	public DayMenu(LocalDate date, RowIterator rowIterator, MealParser mealParser) {
		this.date = date;
		this.rowIterator = rowIterator;
		this.mealParser = mealParser;
	}

	public Iterable<Meal> listMeals() {
		return this::createMealIterator;
	}

	private Iterator<Meal> createMealIterator() {
		return new RecordIterator<>(rowIterator, mealParser.createRecordMatcher(), mealParser.createInstance(DayMenu.this));
	}

	@Override
	public String toString() {
		return com.google.common.base.MoreObjects.toStringHelper(this)
				.add("date", date)
				.toString();
	}
}