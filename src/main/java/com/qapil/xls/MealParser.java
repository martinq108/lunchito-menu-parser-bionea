package com.qapil.xls;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

public class MealParser {

	private final Map<Pattern, MealType> mealTypes = new LinkedHashMap<>();
	private final int mealTypeColumn;
	private final int mealNameColumn;
	private final Pattern alergensLabelPattern;
	private final int alergensLabelColumn;
	private final int alergensValueColumn;

	public MealParser() {
		this.mealTypeColumn = 1;
		this.mealNameColumn = 1;
		this.alergensLabelPattern = Pattern.compile("Obsažené alergeny.*");
		this.alergensLabelColumn = 0;
		this.alergensValueColumn = 2;
	}

	public void addMealType(Pattern pattern, MealType mealType) {
		mealTypes.put(pattern, mealType);
	}

	public RecordMatcher createRecordMatcher() {
		StringCellValueProvider valueProvider = new StringCellValueProvider(mealTypeColumn);
		PatternRecordMatcher recordMatcher = new PatternRecordMatcher(valueProvider);
		mealTypes.keySet().forEach(pattern -> recordMatcher.addPattern(pattern));
		return recordMatcher;
	}

	public RecordFactory<Meal> createInstance(DayMenu dayMenu) {
		return new MealParserInstance(dayMenu);
	}


	private class MealParserInstance implements RecordFactory<Meal> {

		private final DayMenu dayMenu;

		public MealParserInstance(DayMenu dayMenu) {
			this.dayMenu = dayMenu;
		}

		@Override
		public Meal createRecord(RowIterator rowIterator) {
			MealType mealType = parseMealType(rowIterator.next());
			String name = parseMealName(rowIterator);
			String description = parseDescription(rowIterator);
			String nameEng = parseMealName(rowIterator);
			String descriptionEng = parseDescription(rowIterator);
			String alergens = parseAlergens(rowIterator);

			return new Meal(dayMenu, mealType, name, description, alergens, nameEng, descriptionEng);
		}

		private MealType parseMealType(Row row) {
			StringCellValueProvider valueProvider = new StringCellValueProvider(mealTypeColumn);
			String value = valueProvider.getValue(row);
			if (value != null) {
				for (Map.Entry<Pattern, MealType> entry : mealTypes.entrySet()) {
					if (entry.getKey().matcher(value).matches()) {
						return entry.getValue();
					}
				}
			}
			return null;
		}

		private String parseMealName(RowIterator rowIterator) {
			return parseString(rowIterator, true, mealNameColumn);
		}

		private String parseDescription(RowIterator rowIterator) {
			return parseString(rowIterator, false, mealNameColumn);
		}

		private String parseAlergens(RowIterator rowIterator) {
			StringCellValueProvider valueProvider = new StringCellValueProvider(alergensLabelColumn);
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				String value = valueProvider.getValue(row);
				if (value != null && alergensLabelPattern.matcher(value).matches()) {
					return new StringCellValueProvider(alergensValueColumn).getValue(row);
				}
			}
			throw new IllegalStateException("Cannot parse alergens - unexpected end of sheet");
		}

		private String parseString(RowIterator rowIterator, boolean bold, int column) {
			StringBuilder out = new StringBuilder();
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				if (row == null) {
					rowIterator.revert();
					break;
				}
				Cell cell = row.getCell(column);
				if (cell == null) {
					rowIterator.revert();
					break;
				}
				String value = cell.getStringCellValue();
				if (value != null && !value.trim().isEmpty() && getFont(cell).getBold() == bold) {
					out.append(value);
				} else {
					rowIterator.revert();
					break;
				}
			}
			return out.toString();
		}

		private Font getFont(Cell cell) {
			short fontIndex = cell.getCellStyle().getFontIndex();
			return cell.getSheet().getWorkbook().getFontAt(fontIndex);
		}
	}
}
