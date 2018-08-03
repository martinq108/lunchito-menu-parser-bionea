package com.qapil.xls;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DayParser {

	private final Pattern datePattern;
	private final int dateColumn;

	public DayParser(String datePattern, int dateColumn) {
		this.datePattern = Pattern.compile(datePattern);
		this.dateColumn = dateColumn;
	}

	public RecordMatcher createRecordMatcher() {
		StringCellValueProvider valueProvider = new StringCellValueProvider(dateColumn);
		return new PatternRecordMatcher(valueProvider, datePattern);
	}

	public LocalDate parseDate(RowIterator rowIterator) {
		StringCellValueProvider valueProvider = new StringCellValueProvider(dateColumn);
		String value = valueProvider.getValue(rowIterator.next());
		Matcher matcher = datePattern.matcher(value);
		if (!matcher.matches()) {
			throw new IllegalStateException("Cannot extract date cell value");
		}
		int day = Integer.parseInt(matcher.group(1));
		int month = Integer.parseInt(matcher.group(2));
		int year = Integer.parseInt(matcher.group(3));
		return LocalDate.of(year, month, day);
	}
}
