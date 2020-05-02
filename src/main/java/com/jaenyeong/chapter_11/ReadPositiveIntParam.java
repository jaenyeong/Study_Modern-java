package com.jaenyeong.chapter_11;

import org.junit.Test;

import java.util.Optional;
import java.util.Properties;

import static java.util.Optional.*;
import static org.junit.Assert.assertEquals;

public class ReadPositiveIntParam {

	@Test
	public void testMap() {
		Properties props = new Properties();
		props.setProperty("a", "5");
		props.setProperty("b", "true");
		props.setProperty("c", "-3");

		// 지속 시간은 양수여야 하므로 문자열이 양의 정수를 가리키면 해당 정수를 반환하지만 그 외는 0을 반환
		// 프로퍼티 'a'는 양수로 변환할 수 있는 문자열을 포함하므로 readDuration 5를 반환
		assertEquals(5, readDurationImperative(props, "a"));
		// 프로퍼티 'b'는 양수로 변환할 수 없는 문자열을 포함하므로 0 반환
		assertEquals(0, readDurationImperative(props, "b"));
		// 프로퍼티 'c'는 음수 문자열을 포함하므로 0 반환
		assertEquals(0, readDurationImperative(props, "c"));
		// 프로퍼티 'd'라는 이름의 프로퍼티는 없으므로 0 반환
		assertEquals(0, readDurationImperative(props, "d"));

		assertEquals(5, readDurationWithOptional(props, "a"));
		assertEquals(0, readDurationWithOptional(props, "b"));
		assertEquals(0, readDurationWithOptional(props, "c"));
		assertEquals(0, readDurationWithOptional(props, "d"));
	}

	// 지속 시간을 읽는 메서드 시그니처
	public static int readDurationImperative(Properties props, String name) {
		String value = props.getProperty(name);
		// 요청한 이름에 해당하는 프로퍼티가 존재하는지 확인
		if (value != null) {
			try {
				// 문자열 프로퍼티를 숫자로 변환하기 위해 시도
				int i = Integer.parseInt(value);
				// 결과 숫자가 양수인지 확인
				if (i > 0) {
					return i;
				}
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}
		}
		// 하나의 조건이라도 실패하면 0을 반환
		return 0;
	}

	public static int readDurationWithOptional(Properties props, String name) {
		// Properties.getProperty(String)은 null을 반환하므로
		// ofNullable 팩토리 메서드를 이용 Optional을 반환하도록 변경 가능
		return ofNullable(props.getProperty(name))
				// OptionalUtility 클래스 stringToInt 유틸리티 메서드 참조를 전달
				// Optional<String>을 Optional<Integer>로 변경 가능
				.flatMap(ReadPositiveIntParam::s2i)
				// 음수를 필터링해서 제거
				.filter(i -> i > 0).orElse(0);
	}

	// OptionalUtility 클래스 stringToInt 유틸리티 메서드 구현
	public static Optional<Integer> s2i(String s) {
		try {
			return of(Integer.parseInt(s));
		} catch (NumberFormatException e) {
			return empty();
		}
	}
}
