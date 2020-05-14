package com.jaenyeong.chapter_16.OnlineStore.DiscountCode;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class BestPriceFinder {
	private final List<Shop> shops = Arrays.asList(
			new Shop("BestPrice"),
			new Shop("LetsSaveBig"),
			new Shop("MyFavoriteShop"),
			new Shop("BuyItAll"),
			new Shop("ShopEasy"));

	// 커스텀 Executor
	private final Executor executor =
			Executors.newFixedThreadPool(Math.min(shops.size(), 100), (Runnable r) -> {
				Thread t = new Thread(r);
				// 프로그램 종료를 방해하지 않는 데몬 스레드를 사용함
				t.setDaemon(true);
				return t;
			});

	public List<String> findPricesSequential(String product) {
		return shops.stream()
				// 각 상점에서 할인 전 가격 얻기
				.map(shop -> shop.getPrice(product))
				// 상점에서 반환한 문자열을 Quote 객체로 변환
				.map(Quote::parse)
				// Discount 서비스를 이용해서 각 Quote에 할인을 적용
				.map(Discount::applyDiscount)
				.collect(toList());
	}

	public List<String> findPricesParallel(String product) {
		return shops.parallelStream()
				.map(shop -> shop.getPrice(product))
				.map(Quote::parse)
				.map(Discount::applyDiscount)
				.collect(Collectors.toList());
	}

	public List<String> findPricesCompletableFuture(String product) {
		List<CompletableFuture<String>> priceFutures =
				shops.stream()
						.map(shop -> CompletableFuture.supplyAsync(
								() -> shop.getPrice(product), executor))
						.map(future -> future.thenApply(Quote::parse))
						.map(future -> future.thenCompose(
								quote -> CompletableFuture.supplyAsync(
										() -> Discount.applyDiscount(quote), executor)))
						.collect(toList());

		return priceFutures.stream()
				.map(CompletableFuture::join)
				.collect(toList());
	}
}
