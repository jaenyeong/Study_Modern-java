package com.jaenyeong.chapter_09.Logging;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class Peek {

	public static void main(String[] args) {
		List<Integer> numbers = Arrays.asList(2, 3, 4, 5);

		List<Integer> result = numbers.stream()
				.peek(x -> System.out.println("from stream: " + x))
				.map(x -> x + 17)
				.peek(x -> System.out.println("after map: " + x))
				.filter(x -> x % 2 == 0)
				.peek(x -> System.out.println("after filter: " + x))
				.limit(3)
				.peek(x -> System.out.println("after limit: " + x))
				.collect(toList());

		List<Integer> result2 = Stream.of(2, 3, 4, 5)
				.peek(x -> System.out.println("taking from stream: " + x))
				.map(x -> x + 17)
				.peek(x -> System.out.println("after map: " + x))
				.filter(x -> x % 2 == 0)
				.peek(x -> System.out.println("after filter: " + x))
				.limit(3)
				.peek(x -> System.out.println("after limit: " + x))
				.collect(toList());
	}
}
