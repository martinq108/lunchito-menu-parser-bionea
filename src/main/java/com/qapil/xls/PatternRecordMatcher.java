package com.qapil.xls;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Row;

public class PatternRecordMatcher implements RecordMatcher {

	private final ValueProvider<String> valueProvider;
	private final List<Pattern> patterns;

	public PatternRecordMatcher(ValueProvider<String> valueProvider, Pattern... patterns) {
		this.valueProvider = valueProvider;
		this.patterns = new ArrayList<>();
		for (Pattern pattern : patterns) {
			addPattern(pattern);
		}
	}

	public void addPattern(Pattern pattern) {
		patterns.add(pattern);
	}

	@Override
	public boolean matches(Row row) {
		String value = valueProvider.getValue(row);
		if (value != null) {
			for (Pattern p : patterns) {
				if (p.matcher(value).matches()) {
					return true;
				}
			}
		}
		return false;
	}
}
