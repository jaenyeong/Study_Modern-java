package com.jaenyeong.chapter_06;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;

import static java.util.stream.Collector.Characteristics.*;

public class ToListCollector<T> implements Collector<T, List<T>, List<T>> {

	@Override
	public Supplier<List<T>> supplier() {
		// 수집 연산의 시발점
		return ArrayList::new;
	}

	@Override
	public BiConsumer<List<T>, T> accumulator() {
		// 탐색한 항목을 누적하고 바로 누적자를 고침
		return List::add;
	}

	@Override
	public Function<List<T>, List<T>> finisher() {
		// 항등 함수
		return Function.identity();
	}

	@Override
	public BinaryOperator<List<T>> combiner() {
		return (list1, list2) -> { // 두 번째 콘텐츠와 합쳐서 첫 번째 누적자를 고침
			list1.addAll(list2);   // 변경된 첫 번째 누적자를 반환
			return list1;
		};
	}

	@Override
	public Set<Characteristics> characteristics() {
		// 컬렉터의 플래그를 IDENTITY_FINISH, CONCURRENT로 설정
		return Collections.unmodifiableSet(EnumSet.of(IDENTITY_FINISH, CONCURRENT));
	}
}
