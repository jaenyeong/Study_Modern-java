package com.jaenyeong.appendix.C;

import com.jaenyeong.appendix.C.Dish.Dish;
import com.jaenyeong.appendix.C.StreamForkerExample.Results;
import com.jaenyeong.appendix.C.StreamForkerExample.StreamForker;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static com.jaenyeong.appendix.C.Dish.Dish.menu;
import static java.util.stream.Collectors.*;

public class StreamForkerMain {

	public static void main(String[] args) {
		Stream<Dish> menuStream = menu.stream();

		Results results = new StreamForker<Dish>(menuStream)
				.fork("shortMenu",
						s -> s.map(Dish::getName).collect(joining(", ")))
				.fork("totalCalories",
						s -> s.mapToInt(Dish::getCalories).sum())
				.fork("mostCaloricDish",
//						s -> s.collect(reducing((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2)).get())
						s -> s.reduce((d1, d2) -> d1.getCalories() > d2.getCalories() ? d1 : d2).get())
				.fork("dishesByType",
						s -> s.collect(groupingBy(Dish::getType)))
				.getResults();

		String shortMenu = results.get("shortMenu");
		int totalCalories = results.get("totalCalories");
		Dish mostCaloricDish = results.get("mostCaloricDish");
		Map<Dish.Type, List<Dish>> dishesByType = results.get("dishesByType");

		System.out.println("Short menu: " + shortMenu);
		System.out.println("Total calories: " + totalCalories);
		System.out.println("Most caloric dish: " + mostCaloricDish);
		System.out.println("Dishes by type: " + dishesByType);
	}
}
