package com.qapil.xls;

public interface RecordFactory<T> {

	T createRecord(RowIterator rowIterator);
}