package com.qapil.xls;

import java.io.File;
import java.util.regex.Pattern;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class LunchReaderApp {

	public static void main(String[] args) throws Exception {
		new LunchReaderApp().run(args);
	}

	private void run(String[] args) throws Exception {
		if (args.length == 0) {
			System.exit(1);
		}
		File file = new File(args[0]);
		Workbook workbook = WorkbookFactory.create(file);

		DayParser dayParser = new DayParser("[^\\d]*(\\d+). *(\\d+). *(\\d+) */.*", 0);

		MealParser mealParser = new MealParser();
		mealParser.addMealType(Pattern.compile("Polévka.*"), MealType.SOUP);
		mealParser.addMealType(Pattern.compile("Hlavní jídlo.*"), MealType.MEAT);
		mealParser.addMealType(Pattern.compile("Vegetariánské hlavní jídlo.*"), MealType.VEGETARIAN);
		mealParser.addMealType(Pattern.compile("Ňamina pro mlsounka.*"), MealType.SWEET);

		MenuParser menuParser = new MenuParser(dayParser, mealParser);

		RowIterator rowIterator = new RowIterator(workbook.getSheetAt(0));
		for (DayMenu dayMenu : menuParser.parse(rowIterator)) {
			System.out.println(dayMenu);
			for (Meal meal : dayMenu.listMeals()) {
				System.out.println("  " + meal);
			}
		}
	}

}