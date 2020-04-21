package com.jaenyeong.chapter_06;

import java.util.*;

import static java.util.stream.Collectors.groupingBy;

public class GroupingTransaction {

	public static void main(String[] args) {
		groupImperatively();
		groupFunctionally();
	}

	private static void groupImperatively() {
		// 그룹화한 트랜잭션을 저장할 맵 생성
		Map<Currency, List<Transaction>> transactionsByCurrencies = new HashMap<>();

		// 트랜잭션 리스트 반복
		for (Transaction transaction : transactions) {
			Currency currency = transaction.getCurrency();
			List<Transaction> transactionsForCurrency = transactionsByCurrencies.get(currency);

			// 현재 통화를 그룹화하는 맵에 항목이 없으면 항목 생성
			if (transactionsForCurrency == null) {
				transactionsForCurrency = new ArrayList<>();
				transactionsByCurrencies.put(currency, transactionsForCurrency);
			}

			// 같은 통화를 가진 트랜잭션 리스트에 현재 탐색 중인 트랜잭션을 추가
			transactionsForCurrency.add(transaction);
		}

		System.out.println(transactionsByCurrencies);
	}

	private static void groupFunctionally() {
		Map<Currency, List<Transaction>> transactionsByCurrencies =
				transactions.stream().collect(groupingBy(Transaction::getCurrency));

		System.out.println(transactionsByCurrencies);
	}

	public static List<Transaction> transactions = Arrays.asList(
			new Transaction(Currency.EUR, 1500.0),
			new Transaction(Currency.USD, 2300.0),
			new Transaction(Currency.GBP, 9900.0),
			new Transaction(Currency.EUR, 1100.0),
			new Transaction(Currency.JPY, 7800.0),
			new Transaction(Currency.CHF, 6700.0),
			new Transaction(Currency.EUR, 5600.0),
			new Transaction(Currency.USD, 4500.0),
			new Transaction(Currency.CHF, 3400.0),
			new Transaction(Currency.GBP, 3200.0),
			new Transaction(Currency.USD, 4600.0),
			new Transaction(Currency.JPY, 5700.0),
			new Transaction(Currency.EUR, 6800.0)
	);

	public static class Transaction {
		private final Currency currency;
		private final double value;

		public Transaction(Currency currency, double value) {
			this.currency = currency;
			this.value = value;
		}

		public Currency getCurrency() {
			return currency;
		}

		public double getValue() {
			return value;
		}

		@Override
		public String toString() {
			return currency + " " + value;
		}
	}

	public enum Currency {
		EUR, USD, JPY, GBP, CHF
	}
}
