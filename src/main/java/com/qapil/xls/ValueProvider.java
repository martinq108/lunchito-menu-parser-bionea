package com.qapil.xls;

import org.apache.poi.ss.usermodel.Row;

public interface ValueProvider<T> {

	T getValue(Row row);
}
