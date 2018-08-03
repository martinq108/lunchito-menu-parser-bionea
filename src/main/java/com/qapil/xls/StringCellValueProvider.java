package com.qapil.xls;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;

public class StringCellValueProvider implements ValueProvider<String> {

	private final int cellNum;

	public StringCellValueProvider(int cellNum) {
		this.cellNum = cellNum;
	}

	@Override
	public String getValue(Row row) {
		if (row != null) {
			Cell cell = row.getCell(cellNum);
			if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
				return cell.getStringCellValue();
			}
		}
		return null;
	}
}
