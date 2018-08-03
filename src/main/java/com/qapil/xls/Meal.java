package com.qapil.xls;

import com.google.common.base.MoreObjects;

public class Meal {

	private static final int MEALNAME_COLUMN = 1;

	private final DayMenu dayMenu;
	private MealType mealType;
	private String name;
	private String description;
	private String nameEng;
	private String descriptionEng;
	private String alergens;

	public Meal(DayMenu dayMenu, MealType mealType, String name, String description, String alergens, String nameEng, String descriptionEng) {
		this.dayMenu = dayMenu;
		this.mealType = mealType;
		this.name = name;
		this.description = description;
		this.alergens = alergens;
		this.nameEng = nameEng;
		this.descriptionEng = descriptionEng;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
				.add("mealType", mealType)
				.add("name", name)
				.add("description", description)
				.add("nameEng", nameEng)
				.add("descriptionEng", descriptionEng)
				.add("alergens", alergens)
				.toString();
	}

}
