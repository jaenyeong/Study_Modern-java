package com.jaenyeong.chapter_16.OnlineStore.Refactoring;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Stream;

public class BestPriceFinder {
	private final List<Shop> shops = Arrays.asList(
			new Shop("BestPrice"),
			new Shop("LetsSaveBig"),
			new Shop("MyFavoriteShop"),
			new Shop("BuyItAll"),
			new Shop("ShopEasy"));

	private final Executor executor =
			Executors.newFixedThreadPool(Math.min(shops.size(), 100), (Runnable r) -> {
				Thread t = new Thread(r);
				// 프로그램 종료를 방해하지 않는 데몬 스레드를 사용함
				t.setDaemon(true);
				return t;
			});

	// 리팩터링 메서드
	public Stream<CompletableFuture<String>> findPricesStream(String product) {
		return shops.stream()
				.map(shop -> CompletableFuture.supplyAsync(
						() -> shop.getPrice(product), executor))
				.map(future -> future.thenApply(Quote::parse))
				.map(future -> future.thenCompose(
						quote -> CompletableFuture.supplyAsync(
								() -> Discount.applyDiscount(quote), executor)));
	}

	public void printPricesStream(String product) {
		long start = System.nanoTime();

//		CompletableFuture[] futures = findPricesStream(product)
//				.map(f -> f.thenAccept(System.out::println))
//				.toArray(size -> new CompletableFuture[size]);

		CompletableFuture[] futures = findPricesStream(product)
				.map(f -> f.thenAccept(s -> printStream(s, start)))
				.toArray(CompletableFuture[]::new);
		CompletableFuture.allOf(futures).join();

		System.out.println("All shops have now responded in " + ((System.nanoTime() - start) / 1_000_000) + " msecs");
	}

	private void printStream(String s, long start) {
		System.out.println(s + " (done in " + ((System.nanoTime() - start) / 1_000_000) + " msecs)");
	}
}
