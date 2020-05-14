package com.jaenyeong.chapter_16.OnlineStore.DiscountCode;

import java.util.List;
import java.util.function.Supplier;

public class Client {
	private static final BestPriceFinder bestPriceFinder = new BestPriceFinder();

	public static void main(String[] args) {
		// 순차
		execute("price Sequential", () -> bestPriceFinder.findPricesSequential("myPhone27S"));
		// 병렬
		execute("price parallel", () -> bestPriceFinder.findPricesParallel("myPhone27S"));
		// CompletableFuture
		execute("price CompletableFuture", () -> bestPriceFinder.findPricesCompletableFuture("myPhone27S"));
	}

	private static void execute(String msg, Supplier<List<String>> s) {
		long start = System.nanoTime();
		System.out.println(s.get());
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println(msg + " done in " + duration + " msecs");
	}
}
