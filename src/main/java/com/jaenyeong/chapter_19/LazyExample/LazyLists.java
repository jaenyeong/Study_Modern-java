package com.jaenyeong.chapter_19.LazyExample;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class LazyLists {

	public static void main(String[] args) {
		MyList<Integer> l = new MyLinkedList<>(10, new Empty<>());

		LazyList<Integer> numbers = from(2);
		int two = numbers.head();
		int three = numbers.tail().head();
		int four = numbers.tail().tail().head();

		System.out.println(two + " " + three + " " + four);

		// 소수 출력
		int primeTwo = primes(numbers).head();
		int primeThree = primes(numbers).tail().head();
		int primeFive = primes(numbers).tail().tail().head();

		System.out.println(primeTwo + " " + primeThree + " " + primeFive);

//		printAll(primes(from(2)));
	}

	interface MyList<T> {
		T head();

		MyList<T> tail();

		default boolean isEmpty() {
			return true;
		}

		// 추가
		MyList<T> filter(Predicate<T> p);
	}

	static class MyLinkedList<T> implements MyList<T> {
		private final T head;
		private final MyList<T> tail;

		public MyLinkedList(T head, MyList<T> tail) {
			this.head = head;
			this.tail = tail;
		}

		@Override
		public T head() {
			return head;
		}

		@Override
		public MyList<T> tail() {
			return tail;
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public MyList<T> filter(Predicate<T> p) {
			return isEmpty() ?
					this :
					p.test(head()) ?
							new MyLinkedList<>(head(), tail().filter(p)) :
							tail().filter(p);
		}
	}

	static class Empty<T> implements MyList<T> {

		@Override
		public T head() {
			throw new UnsupportedOperationException();
		}

		@Override
		public MyList<T> tail() {
			throw new UnsupportedOperationException();
		}

		@Override
		public MyList<T> filter(Predicate<T> p) {
			return this;
		}
	}

	static class LazyList<T> implements MyList<T> {
		final T head;
		final Supplier<MyList<T>> tail;

		public LazyList(T head, Supplier<MyList<T>> tail) {
			this.head = head;
			this.tail = tail;
		}

		@Override
		public T head() {
			return head;
		}

		@Override
		public MyList<T> tail() {
			// 위 head와 달리 tail에서는 Supplier로 게으른 동작을 만듦
			return tail.get();
		}

		@Override
		public boolean isEmpty() {
			return false;
		}

		@Override
		public MyList<T> filter(Predicate<T> p) {
			return isEmpty() ?
					// 새로운 Empty<>()를 반환할 수도 있지만 여기서는 this로 대신할 수 있음
					this :
					p.test(head()) ?
							new LazyList<>(head(), () -> tail().filter(p)) :
							tail().filter(p);
		}
	}

	public static LazyList<Integer> from(int n) {
		return new LazyList<Integer>(n, () -> from(n + 1));
	}

	public static MyList<Integer> primes(MyList<Integer> numbers) {
		return new LazyList<>(
				numbers.head(),
				() -> primes(
						numbers.tail().filter(n -> n % numbers.head() != 0)));
	}

	// 무한 실행
//	static <T> void printAll(MyList<T> list) {
//		while (!list.isEmpty()) {
//			System.out.println(list.head());
//			list = list.tail();
//		}
//	}

	// 위 메서드를 재귀적으로 변경
	// 스택오버플로로 인하여 무한히 호출되지 않음
	static <T> void printAll(MyList<T> list) {
		if (list.isEmpty()) {
			return;
		}

		System.out.println(list.head());
		printAll(list.tail());
	}
}
