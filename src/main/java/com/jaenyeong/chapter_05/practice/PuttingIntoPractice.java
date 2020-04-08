package com.jaenyeong.chapter_05.practice;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class PuttingIntoPractice {

	public static void main(String[] args) {
		Trader raoul = new Trader("Raoul", "Cambridge");
		Trader mario = new Trader("Mario", "Milan");
		Trader alan = new Trader("Alan", "Cambridge");
		Trader brian = new Trader("Brian", "Cambridge");

		List<Transaction> transactions = Arrays.asList(
				new Transaction(brian, 2011, 300),
				new Transaction(raoul, 2012, 1000),
				new Transaction(raoul, 2011, 400),
				new Transaction(mario, 2012, 710),
				new Transaction(mario, 2012, 700),
				new Transaction(alan, 2012, 950)
		);

		/*
		[1] 2011년에 일어난 모든 트랜잭션을 찾아 값을 오름차순으로 정리
		 */
		List<Transaction> t2011 = transactions.stream()
				.filter(t -> t.getYear() == 2011)
				.sorted(comparing(Transaction::getValue))
				.collect(toList());

		System.out.println(t2011);

		/*
		[2] 거래자가 근무하는 모든 도시를 중복 없이 나열
 		 */
		List<String> workCities = transactions.stream()
				.map(t -> t.getTrader().getCity())
				.distinct()
				.collect(toList());

		System.out.println(workCities);

		// 아래와 같이 distinct() 대신에 toSet()을 사용할 수 있음
		Set<String> workCitiesSet = transactions.stream()
				.map(t -> t.getTrader().getCity())
				.collect(toSet());

		System.out.println(workCitiesSet);

		/*
		[3] 케임브리지에서 근무하는 모든 거래자를 찾아서 이름순으로 정렬
		 */
//		List<Trader> traderInCambridge = transactions.stream()
//				.filter(t -> "Cambridge".equals(t.getTrader().getCity()))
//				.map(Transaction::getTrader)
//				.distinct()
//				.sorted(Comparator.comparing(Trader::getName))
//				.collect(toList());
//
//		System.out.println(traderInCambridge);

		List<Trader> traderInCambridge = transactions.stream()
				.map(Transaction::getTrader)
				.filter(t -> "Cambridge".equals(t.getCity()))
				.distinct()
				.sorted(comparing(Trader::getName))
				.collect(toList());

		System.out.println(traderInCambridge);

		/*
		[4] 모든 거래자의 이름을 알파벳순으로 정렬해서 반환
		 */
//		List<String> tradersName = transactions.stream()
//				.map(t -> t.getTrader().getName())
//				.distinct()
//				.sorted()
//				.collect(toList());
//
//		System.out.println(tradersName);

		// 각 반복 과정에서 모든 문자열을 반복적으로 연결해서 새로운 문자열 객체를 만듦
		// 따라서 아래 코드는 비효율적
//		String tradersName = transactions.stream()
//				.map(t -> t.getTrader().getName())
//				.distinct()
//				.sorted()
//				.reduce("", (n1, n2) -> n1 + n2);

		// 위 코드를 아래와 같이 joining()을 이용하여 해결
		// joining()은 내부적으로 StringBuilder를 이용
		String tradersName = transactions.stream()
				.map(t -> t.getTrader().getName())
				.distinct()
				.sorted()
				.collect(joining());

		System.out.println(tradersName);

		/*
		[5] 밀라노에 거래자가 있는가
		 */
//		Optional<Trader> traderInMilan = transactions.stream()
//				.filter(t -> "Milan".equals(t.getTrader().getCity()))
//				.distinct()
//				.map(Transaction::getTrader)
//				.findAny();
//
//		traderInMilan.ifPresent(System.out::println);

		boolean milanBased = transactions.stream()
				.anyMatch(t -> "Milan".equals(t.getTrader().getCity()));

		System.out.println(milanBased);

		/*
		[6] 케임브리지에 거주하는 거래자의 모든 트랜잭션 값을 추출
		 */
//		List<Transaction> cambridgeTransaction = transactions.stream()
//				.filter(t -> "Cambridge".equals(t.getTrader().getCity()))
//				.distinct()
//				.collect(toList());
//
//		System.out.println(cambridgeTransaction);

		transactions.stream()
				.filter(t -> "Cambridge".equals(t.getTrader().getCity()))
				.map(Transaction::getValue)
				.forEach(System.out::println);

		/*
		[7] 전체 트랜잭션 중 최대값은?
		 */
//		Optional<Integer> maxValue = transactions.stream()
//				.map(Transaction::getValue)
//				.max(Comparator.naturalOrder());
//
//		maxValue.ifPresent(System.out::println);

		Optional<Integer> maxValue = transactions.stream()
				.map(Transaction::getValue)
				.reduce(Integer::max);

		maxValue.ifPresent(System.out::println);

		/*
		[8] 전체 트랜잭션 중 최솟값은?
		 */
//		Optional<Integer> minValue = transactions.stream()
//				.map(Transaction::getValue)
//				.min(Comparator.naturalOrder());
//
//		minValue.ifPresent(System.out::println);

		Optional<Transaction> minTransaction = transactions.stream()
				.reduce((t1, t2) -> t1.getValue() < t2.getValue() ? t1 : t2);

		minTransaction.ifPresent(System.out::println);

		// 스트림은 최대값이나 최솟값을 계산하는 데 사용할 키를 지정하는 Comparator를 인수로 받는 min, max 메서드를 제공
		Optional<Transaction> smallestTransaction = transactions.stream()
				.min(comparing(Transaction::getValue));

		smallestTransaction.ifPresent(System.out::println);
	}
}
