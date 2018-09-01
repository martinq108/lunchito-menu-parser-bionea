package com.qapil.xls;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

class RowIterator implements Iterator<Row> {

	private final Sheet sheet;
	private final int firstRow;
	private final int lastRow;
	private int rowNum;

	public RowIterator(Sheet sheet) {
		this(sheet, 0, sheet.getLastRowNum());
	}

	public RowIterator(Sheet sheet, int firstRow, int lastRow) {
		this.sheet = sheet;
		this.firstRow = firstRow;
		this.lastRow = lastRow;
		this.rowNum = firstRow;
	}

	@Override
	public boolean hasNext() {
		return rowNum <= lastRow;
	}

	@Override
	public Row next() {
		if (rowNum > lastRow) {
			throw new NoSuchElementException("No more rows for the record");
		}
		Row row = sheet.getRow(rowNum++);
		return row;
//		Row row = sheet.getRow(rowNum++);
//		if (row != null) {
//			Cell cell = row.getCell(1);
//			if (cell != null) {
//				CellStyle cellStyle = cell.getCellStyle();
//				Font font = cell.getSheet().getWorkbook().getFontAt(cellStyle.getFontIndex());
//				System.out.println(MessageFormat.format("x: {0}\t{1}\t{2}",
//						cellStyle.getFontIndex(), font.getBold(), cell.getStringCellValue()));
//			}
//		}
//		return row;
	}

	/**
	 * Returns the row number of the last returned row (by the <code>next()</code> method).
	 * @return row number or <code>-1</code> if no row has been returned by this iterator
	 */
	public int getLastRowNum() {
		return rowNum > firstRow ? rowNum - 1 : -1;
	}

	/**
	 * Returns current row of the iterator (last returned by <code>next()</code> method).
	 * @return last returned row or <code>null</code> if no row has been returned by this iterator
	 */
	public Row getLastRow() {
		int currentRow = getLastRowNum();
		return currentRow > -1 ? sheet.getRow(currentRow) : null;
	}

	/**
	 * Returns the sheet that this row iterator iterates.
	 */
	public Sheet getSheet() {
		return sheet;
	}

	public int revert() {
		if (rowNum > firstRow) {
			return --rowNum;
		} else {
			throw new IllegalStateException("Cannot revert row iterator, because it is already before its first record.");
		}
	}
}