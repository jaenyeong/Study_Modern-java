package com.jaenyeong.chapter_10.DSLPatternExample;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

// [세 번째] 고객이 요청한 한 개 이상의 거래의 주문
public class Order {
	private String customer;
	private List<Trade> trades = new ArrayList<>();

	public String getCustomer() {
		return customer;
	}

	public Order setCustomer(String customer) {
		this.customer = customer;
		return this;
	}

	public void addTrade(Trade trade) {
		trades.add(trade);
	}

	public double getValue() {
		return trades.stream().mapToDouble(Trade::getValue).sum();
	}

	@Override
	public String toString() {
		String strTrades = trades.stream()
				.map(t -> "  " + t)
				.collect(joining("\n", "[\n", "\n]"));
		return String.format("Order[customer=%s, trades=%s]", customer, strTrades);
	}
}
