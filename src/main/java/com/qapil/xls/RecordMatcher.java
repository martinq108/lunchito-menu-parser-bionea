package com.qapil.xls;

import org.apache.poi.ss.usermodel.Row;

public interface RecordMatcher {

	boolean matches(Row row);
}
