package com.qapil.xls;

import java.time.LocalDate;
import java.util.Iterator;

public class MenuParser {

	private final DayParser dayParser;
	private final MealParser mealParser;

	public MenuParser(DayParser dayParser, MealParser mealParser) {
		this.dayParser = dayParser;
		this.mealParser = mealParser;
	}

	public Iterable<DayMenu> parse(RowIterator rowIterator) {
		return new MenuCollection(rowIterator);
	}

	private class MenuCollection implements Iterable<DayMenu> {

		private final RowIterator rowIterator;

		public MenuCollection(RowIterator rowIterator) {
			this.rowIterator = rowIterator;
		}

		@Override
		public Iterator<DayMenu> iterator() {
			RecordMatcher dayMatcher = dayParser.createRecordMatcher();
			return new RecordIterator<>(rowIterator, dayMatcher, this::createDayMenu);
		}

		private DayMenu createDayMenu(RowIterator rowIterator) {
			LocalDate date = dayParser.parseDate(rowIterator);
			return new DayMenu(date, rowIterator, mealParser);
		}
	}

}