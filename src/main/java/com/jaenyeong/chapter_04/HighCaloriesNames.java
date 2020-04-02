package com.jaenyeong.chapter_04;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static com.jaenyeong.chapter_04.Dish.*;

public class HighCaloriesNames {

	public static void main(String[] args) {
		List<String> names = menu.stream()
				.filter(dish -> {
					System.out.println("filtering " + dish.getName());
					return dish.getCalories() > 300;
				})
				.map(dish -> {
					System.out.println("mapping " + dish.getName());
					return dish.getName();
				})
				.limit(3)
				.collect(toList());
		System.out.println(names);
		// filtering pork
		// mapping pork
		// filtering beef
		// mapping beef
		// filtering chicken
		// mapping chicken
		// [pork, beef, chicken]
	}
}
