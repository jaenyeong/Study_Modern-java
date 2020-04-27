package com.jaenyeong.chapter_09;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.jaenyeong.chapter_09.Dish.menu;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingInt;

public class Refactoring {

	private void anonymousClass() {
		Runnable r1 = new Runnable() {
			@Override
			public void run() {
				System.out.println("Hello1");
			}
		};

		Runnable r2 = () -> System.out.println("Hello2");
	}

	private void shadowVariable() {
//		int a = 10;
//		Runnable r1 = () -> {
//			int a = 2; // 컴파일 에러
//			System.out.println(a);
//		};

		Runnable r2 = new Runnable() {
			@Override
			public void run() {
				int a = 2; // 작동
				System.out.println(a);
			}
		};
	}

	private void callTask() {
		// Task를 구현하는 익명 클래스 전달
		doSomething(new Task() {
			@Override
			public void execute() {
				System.out.println("Danger danger!!");
			}
		});

		// doSomething(Runnable), doSomething(Task) 메서드 모두 대상 형식이 되서 문제
//		doSomething(() -> System.out.println("Danger"));

		// 명시적 해결
		doSomething((Task) () -> System.out.println("Danger"));
	}

	interface Task {
		public void execute();
	}

	public static void doSomething(Runnable r) {
		r.run();
	}

	public static void doSomething(Task t) {
		t.execute();
	}

	enum CaloricLevel {DIET, NORMAL, FAT}

	private void caloricExample() {
		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel =
				menu.stream()
						.collect(groupingBy(dish -> {
							if (dish.getCalories() <= 400) return CaloricLevel.DIET;
							else if (dish.getCalories() <= 700) return CaloricLevel.NORMAL;
							else return CaloricLevel.FAT;
						}));

		Map<CaloricLevel, List<Dish>> dishesByCaloricLevel2 =
				menu.stream().collect(groupingBy(Dish::getCaloricLevel));
	}

	List<Apple> inventory = Arrays.asList(
			new Apple(80, Color.GREEN),
			new Apple(155, Color.GREEN),
			new Apple(120, Color.RED)
	);

	private void methodReferenceExample() {
		inventory.sort((Apple a1, Apple a2) -> a1.getWeight().compareTo(a2.getWeight()));
		inventory.sort(comparing(Apple::getWeight));
	}

	private void reduceExample() {
		int totalCalories = menu.stream().map(Dish::getCalories).reduce(0, (c1, c2) -> c1 + c2);
		int totalCalories2 = menu.stream().collect(summingInt(Dish::getCalories)); // 코드가 문제 자체를 설명함
	}

	private void executeAround() throws IOException {
		String oneLine = processFile((BufferedReader b) -> b.readLine()); // 람다 전달
		String twoLines = processFile((BufferedReader b) -> b.readLine() + b.readLine()); // 다른 람다 전달
	}

	// IOException을 던질 수 있는 람다의 함수형 인터페이스
	public static String processFile(BufferedReaderProcessor p) throws IOException {
		try (BufferedReader br = new BufferedReader(new FileReader("com/jaenyeong/chapter_09/data.txt"))) {
			return p.process(br); // 인수로 전달된 BufferedReaderProcessor를 실행
		}
	}

	@FunctionalInterface
	public interface BufferedReaderProcessor {
		String process(BufferedReader b) throws IOException;
	}
}
