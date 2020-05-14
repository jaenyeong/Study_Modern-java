package com.jaenyeong.chapter_16.OnlineStore.Origin;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import static java.util.stream.Collectors.toList;

public class BestPriceFinder {
	// 4개 상점일 때
//	List<Shop> shops = Arrays.asList(
//			new Shop("BestPrice"),
//			new Shop("LetsSaveBig"),
//			new Shop("MyFavoriteShop"),
//			new Shop("ButItAll")
//	);

	// 5개 상점일 때
	private final List<Shop> shops = Arrays.asList(
			new Shop("BestPrice"),
			new Shop("LetsSaveBig"),
			new Shop("MyFavoriteShop"),
			new Shop("BuyItAll"),
			new Shop("ShopEasy"));

	// 커스텀 Executor
//	private final Executor executor =
//			Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
//				@Override
//				public Thread newThread(Runnable r) {
//					Thread t = newThread(r);
//					// 프로그램 종료를 방해하지 않는 데몬 스레드를 사용함
//					t.setDaemon(true);
//					return t;
//				}
//			});

	// 커스텀 Executor 람다 사용
	private final Executor executor =
			Executors.newFixedThreadPool(Math.min(shops.size(), 100), (Runnable r) -> {
				Thread t = new Thread(r);
				// 프로그램 종료를 방해하지 않는 데몬 스레드를 사용함
				t.setDaemon(true);
				return t;
			});

	// 순차
	public List<String> findPricesSequential(String product) {
		return shops.stream()
				.map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
				.collect(toList());
	}

	// 병렬
	public List<String> findPricesParallel(String product) {
		return shops.parallelStream()
				.map(shop -> String.format("%s price is %.2f", shop.getName(), shop.getPrice(product)))
				.collect(toList());
	}

	// 팩토리 메서드 supplyAsync 이용 CompletableFuture 생성
	public List<String> findPricesFromSupplyAsyncMethod(String product) {
		// 팩토리 메서드 이용
//		CompletableFuture.supplyAsync(() -> shop.getPrice(product), executor);

//		List<CompletableFuture<String>> priceFutures =
//				shops.stream()
//						.map(shop -> CompletableFuture.supplyAsync(
//								() -> String.format("%s prices is %.2f",
//										shop.getName(), shop.getPrice(product))))
//						.collect(toList());

		List<CompletableFuture<String>> priceFutures =
				shops.stream()
						// CompletableFuture로 각각의 가격을 비동기적으로 계산
						.map(shop -> CompletableFuture.supplyAsync(
								() -> shop.getName() + " price is " + shop.getPrice(product)))
						.collect(toList());

		return priceFutures.stream()
				// 모든 비동기 동작이 끝나길 기다림
				.map(CompletableFuture::join)
				.collect(toList());
	}
}
