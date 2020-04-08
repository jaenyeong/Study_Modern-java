package com.jaenyeong.chapter_05;

import java.util.Optional;

import static com.jaenyeong.chapter_05.Dish.menu;

public class Finding {

	public static void main(String[] args) {
		if (isVegetarianFriendlyMenu()) {
			System.out.println("Vegetarian friendly");
		}

		System.out.println(isHealthyMenu());
		System.out.println(isHealthyMenu2());

		Optional<Dish> dish = findVegetarianDish();
		dish.ifPresent(d -> System.out.println(d.getName()));
	}

	private static boolean isVegetarianFriendlyMenu() {
		// anyMatch : 프레디케이트가 적어도 한 요소와 일치하는지 확인
		return menu.stream().anyMatch(Dish::isVegetarian);
	}

	private static boolean isHealthyMenu() {
		// allMatch : 프레디케이트가 모든 요소와 일치하는지 검사
		return menu.stream().allMatch(d -> d.getCalories() < 1000);
	}

	private static boolean isHealthyMenu2() {
		// noneMatch : 주어진 프레디케이트와 일치하는 요소가 없는지 확인 (allMatch와 반대 연산 수행)
		return menu.stream().noneMatch(d -> d.getCalories() >= 1000);
	}

	private static Optional<Dish> findVegetarianDish() {
		// findAny : 현재 스트림에서 임의의 요소를 반환
		return menu.stream().filter(Dish::isVegetarian).findAny();
	}
}
