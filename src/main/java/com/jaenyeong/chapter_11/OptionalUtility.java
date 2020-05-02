package com.jaenyeong.chapter_11;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.of;

public class OptionalUtility {

	public static void main(String[] args) {
		System.out.println(max(of(3), of(5)));
		System.out.println(max(empty(), of(5)));

		Optional<Integer> opt1 = of(5);
		Optional<Integer> opt2 = opt1.or(() -> of(4));

		System.out.println(
				of(5).or(() -> of(4))
		);
	}

	public static void wrapToGetFromMap() {
		Map<String, Object> newMap = new HashMap<>();
		Optional<Object> value = Optional.ofNullable(newMap.get("key"));
	}

	public static Optional<Integer> stringToInt(String s) {
		try {
			// 문자열을 정수로 변환할 수 있으면 정수로 변환된 값을 포함하는 Optional을 반환
			return Optional.of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			e.printStackTrace();
			// 그렇지 않으면 빈 Optional을 반환
			return Optional.empty();
		}
	}

	public static final Optional<Integer> max(Optional<Integer> i, Optional<Integer> j) {
		return i.flatMap(a -> j.map(b -> Math.max(a, b)));
	}
}
