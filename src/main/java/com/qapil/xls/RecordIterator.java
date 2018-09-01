package com.qapil.xls;

import java.util.Iterator;

import org.apache.poi.ss.usermodel.Row;

public class RecordIterator<T> implements Iterator<T> {

	private final RowIterator rowIterator;
	private final RecordMatcher recordMatcher;
	private final RecordFactory<T> recordFactory;
	private boolean prepared = false;
	private int prevMatch = -1;
	private int lastMatch = -1;

	public RecordIterator(RowIterator rowIterator, RecordMatcher recordMatcher, RecordFactory<T> recordFactory) {
		this.rowIterator = rowIterator;
		this.recordMatcher = recordMatcher;
		this.recordFactory = recordFactory;
	}

	@Override
	public boolean hasNext() {
		ensureNext();
		return prepared;
	}

	@Override
	public T next() {
		ensureNext();
		if (prepared) {
			prepared = false;
			return recordFactory.createRecord(new RowIterator(rowIterator.getSheet(), prevMatch, lastMatch-1));
		} else {
			return null;
		}
	}

	private void ensureNext() {
		if (prepared) {
			return;
		}
		while (!prepared && rowIterator.hasNext()) {
			Row row = rowIterator.next();
			if (recordMatcher.matches(row)) {
				prevMatch = lastMatch;
				lastMatch = rowIterator.getLastRowNum();
				if (prevMatch != -1) {
					prepared = true;
				}
			}
		}
		if (!prepared && lastMatch != -1) {
			prevMatch = lastMatch;
			lastMatch = rowIterator.getLastRowNum()+1;
			if (prevMatch != lastMatch) {
				prepared = true;
			}
		}
	}
}