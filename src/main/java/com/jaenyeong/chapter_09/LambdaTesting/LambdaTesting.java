package com.jaenyeong.chapter_09.LambdaTesting;

import java.util.ArrayList;
import java.util.List;

public class LambdaTesting {

	@FunctionalInterface
	public interface GenericPredicate {
		boolean test(Integer a);
	}

	public static List<Integer> filter(List<Integer> inventory, GenericPredicate p) {
		List<Integer> result = new ArrayList<>();
		for (Integer apple : inventory) {
			if (p.test(apple)) {
				result.add(apple);
			}
		}
		return result;
	}
}
