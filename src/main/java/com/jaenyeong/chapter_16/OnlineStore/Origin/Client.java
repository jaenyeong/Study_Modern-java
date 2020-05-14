package com.jaenyeong.chapter_16.OnlineStore.Origin;

import java.util.List;
import java.util.concurrent.Future;
import java.util.function.Supplier;

import static com.jaenyeong.chapter_16.OnlineStore.Origin.Shop.doSomethingElse;

public class Client {
	private static final BestPriceFinder bestPriceFinder = new BestPriceFinder();

	public static void main(String[] args) {
		async();

		// 순차
		execute("sequential", () -> bestPriceFinder.findPricesSequential("myPhone27S"));
		// 병렬 스트림
		execute("parallel", () -> bestPriceFinder.findPricesParallel("myPhone27S"));
		// CompletableFuture
		execute("CompletableFuture", () -> bestPriceFinder.findPricesFromSupplyAsyncMethod("myPhone27S"));
	}

	private static void async() {
		Shop shop = new Shop("BestShop");
		long start = System.nanoTime();

		// 상점에 제품 가격 정보 요청
		Future<Double> futurePrice = shop.getPriceAsyncV1("my favorite product");
		// 예외 발생 확인
//		Future<Double> futurePrice = shop.getPriceAsyncV2("my favorite product");

		long invocationTime = ((System.nanoTime() - start) / 1_000_000);
		System.out.println("Invocation returned after " + invocationTime + " msecs");

		// 제품의 가격을 계산하는 동안
		doSomethingElse();

		// 다른 상점 검색 등 다른 작업 수행
		try {
			double price = futurePrice.get();
			System.out.printf("Price is %.2f%n", price);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
		System.out.println("Price returned after " + retrievalTime + " msecs");
	}

	private static void execute(String msg, Supplier<List<String>> s) {
		long start = System.nanoTime();
		System.out.println(s.get());
		long duration = (System.nanoTime() - start) / 1_000_000;
		System.out.println(msg + " done in " + duration + " msecs");
	}
}
