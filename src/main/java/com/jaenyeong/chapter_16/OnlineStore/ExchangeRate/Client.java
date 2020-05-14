package com.jaenyeong.chapter_16.OnlineStore.ExchangeRate;

import java.util.List;
import java.util.function.Supplier;

public class Client {
	private static final BestPriceFinder bestPriceFinder = new BestPriceFinder();

	public static void main(String[] args) {
		// 순차
		execute("price sequential", () -> bestPriceFinder.findPricesInUSD("myPhone27S"));

		// java7
		execute("price Java7", () -> bestPriceFinder.findPricesInUSDForJava7("myPhone27S"));

		// CompleteFuture Timeout
		execute("price CompleteFuture Timeout", () -> bestPriceFinder.findPricesInUSDTimeOut("myPhone27S"));
	}

	private static void execute(String msg, Supplier<List<String>> s) {
		long start = System.nanoTime();
		System.out.println(s.get());
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println(msg + " done in " + duration + " msecs");
	}
}
