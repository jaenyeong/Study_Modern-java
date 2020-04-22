package com.jaenyeong.chapter_07;

import java.util.stream.LongStream;
import java.util.stream.Stream;

public class ParallelStreams {

	public static long sequentialSum(long n) {
		return Stream.iterate(1L, i -> i + 1) // 무한 자연수 스트림 생성
				.limit(n) // n개 이하로 제한
				.reduce(Long::sum) // 모든 숫자를 더하는 스트림 리듀싱 연산
				.get();
	}

	public static long iterativeSum(long n) {
		long result = 0;
		for (long i = 1L; i <= n; i++) {
			result += i;
		}
		return result;
	}

	public static long parallelSum(long n) {
		return Stream.iterate(1L, i -> i + 1)
				.limit(n)
				.parallel() // 스트림을 병렬 스트림으로 변환
				.reduce(0L, Long::sum);
	}

	public static long rangedSum(long n) {
		return LongStream.rangeClosed(1, n)
				.reduce(0L, Long::sum);
	}

	public static long parallelRangedSum(long n) {
		return LongStream.rangeClosed(1, n)
				.parallel()
				.reduce(0L, Long::sum);
	}

	public static long sideEffectSum(long n) {
		Accumulator accumulator = new Accumulator();
		LongStream.rangeClosed(1, n).forEach(accumulator::add);
		return accumulator.total;
	}

	public static long sideEffectParallelSum(long n) {
		Accumulator accumulator = new Accumulator();
		LongStream.rangeClosed(1, n).parallel().forEach(accumulator::add);
		return accumulator.total;
	}

	public static class Accumulator {
		public long total = 0;

		public void add(long value) {
			total += value;
		}
	}
}
