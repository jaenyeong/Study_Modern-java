package com.jaenyeong.chapter_19.LazyExample;

import java.util.stream.IntStream;
import java.util.stream.Stream;

public class MyMathUtils {

	public static void main(String[] args) {
		IntStream numbers = numbers();
		int head = head(numbers);

		// 예외 발생
		// java.lang.IllegalStateException: stream has already been operated upon or closed
//		IntStream filtered = tail(numbers).filter(n -> n % head != 0);

		// 예외 발생
		// java.lang.IllegalStateException: stream has already been operated upon or closed
//		System.out.println(primes(numbers));
	}

	public static Stream<Integer> primes(int n) {
		return Stream.iterate(2, i -> i + 1)
				.filter(MyMathUtils::isPrime)
				.limit(n);
	}

	public static boolean isPrime(int candidate) {
		int candidateRoot = (int) Math.sqrt((double) candidate);
		return IntStream.rangeClosed(2, candidateRoot)
				.noneMatch(i -> candidate % i == 0);
	}

	// [1] 스트림 숫자 얻기
	static IntStream numbers() {
		return IntStream.iterate(2, n -> n + 1);
	}

	// [2] 머리 획득
	static int head(IntStream numbers) {
		return numbers.findFirst().getAsInt();
	}

	// [3] 꼬리 필터링
	static IntStream tail(IntStream numbers) {
		return numbers.skip(1);
	}

	// [4] 재귀적으로 소수 스트림 생성
	// 이대로 실행시 다음과 같은 예외 발생
	// java.lang.IllegalStateException: stream has already been operated upon or closed
	static IntStream primes(IntStream numbers) {
		int head = head(numbers);
		return IntStream.concat(
				IntStream.of(head),
				primes(tail(numbers).filter(n -> n % head != 0)));
	}

}
