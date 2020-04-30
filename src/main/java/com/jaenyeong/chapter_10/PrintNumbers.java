package com.jaenyeong.chapter_10;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class PrintNumbers {

	public static void main(String[] args) {
		List<String> numbers = Arrays.asList("one", "two", "three");

		// 익명 클래스
		System.out.println("Anonymous class:");
		numbers.forEach(new Consumer<String>() {
			@Override
			public void accept(String s) {
				System.out.println(s);
			}
		});

		// 람다
		System.out.println("Lambda expression:");
		numbers.forEach(s -> System.out.println(s));

		// 메서드 참조
		System.out.println("Method reference:");
		numbers.forEach(System.out::println);
	}
}
