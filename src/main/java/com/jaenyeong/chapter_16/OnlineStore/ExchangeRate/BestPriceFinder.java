package com.jaenyeong.chapter_16.OnlineStore.ExchangeRate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.jaenyeong.chapter_16.OnlineStore.ExchangeRate.ExchangeService.*;

public class BestPriceFinder {
	private final List<Shop> shops = Arrays.asList(
			new Shop("BestPrice"),
			new Shop("LetsSaveBig"),
			new Shop("MyFavoriteShop"),
			new Shop("BuyItAll"),
			new Shop("ShopEasy"));

	public List<String> findPricesInUSD(String product) {

		for (Shop shop : shops) {
			Future<Double> futurePriceInUSD =
					CompletableFuture.supplyAsync(
							// 제품가격 정보를 요청하는 첫 번째 태스크를 생성
							() -> shop.getPrice(product))
							.thenCombine(CompletableFuture.supplyAsync(
									// USD, EUR의 환율 정보를 요청하는 독립적인 두 번째 태스크 생성
									() -> ExchangeService.getRate(Money.EUR, Money.USD)),
									// 두 결과를 곱해서 가격과 환율 정보를 합침
									(price, rate) -> price * rate);
		}

		return null;
	}

	public List<String> findPricesInUSDForJava7(String product) {
		// 태스크를 스레드 풀에 제출할 수 있도록 ExecutorService 생성
		ExecutorService executor = Executors.newCachedThreadPool();
		List<Future<Double>> priceFutures = new ArrayList<>();

		for (Shop shop : shops) {
			// 람다 변경 전
//			final Future<Double> futureRate = executor.submit(new Callable<Double>() {
//				@Override
//				public Double call() {
//					// EUR USD 환율 정보를 가져올 Future 생성
//					return getRate(Money.EUR, Money.USD);
//				}
//			});

			final Future<Double> futureRate = executor.submit(() -> {
				// EUR USD 환율 정보를 가져올 Future 생성
				return getRate(Money.EUR, Money.USD);
			});

			// 람다 변경 전
//			Future<Double> futurePriceInUSD = executor.submit(new Callable<Double>() {
//				@Override
//				public Double call() {
//					try {
//						// 두 번째 Future로 상점에서 요청 제품의 가격을 검색
//						double priceInEUR = shop.getPrice(product);
//						// 가격을 검색한 Future를 이용해 가격과 환율을 곱함
//						return priceInEUR * futureRate.get();
//					} catch (InterruptedException | ExecutionException e) {
//						throw new RuntimeException(e.getMessage(), e);
//					}
//				}
//			});

			Future<Double> futurePriceInUSD = executor.submit(() -> {
				try {
					// 두 번째 Future로 상점에서 요청 제품의 가격을 검색
					double priceInEUR = shop.getPrice(product);
					// 가격을 검색한 Future를 이용해 가격과 환율을 곱함
					return priceInEUR * futureRate.get();
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e.getMessage(), e);
				}
			});

			priceFutures.add(futurePriceInUSD);
		}

		List<String> prices = new ArrayList<>();
		for (Future<Double> priceFuture : priceFutures) {
			try {
				prices.add(/*shop.getName() +*/ " price is " + priceFuture.get());
			} catch (ExecutionException | InterruptedException e) {
				e.printStackTrace();
			}
		}

		executor.shutdown();

		return prices;
	}

	// 타임아웃 추가
	public List<String> findPricesInUSDTimeOut(String product) {
		List<CompletableFuture<Double>> priceFutures = new ArrayList<>();

		// 타임아웃 추가
//		for (Shop shop : shops) {
//			CompletableFuture<Double> futurePriceInUSD =
//					CompletableFuture.supplyAsync(() -> shop.getPrice(product))
//							.thenCombine(CompletableFuture.supplyAsync(
//									() -> ExchangeService.getRate(Money.EUR, Money.USD)),
//									(price, rate) -> price * rate)
//							// 3초 뒤에 작업이 완료되지 않으면 Future가 TimeoutException을 발생시키도록 설정
//							// 자바 9에서는 비동기 타임아웃 관리 기능이 추가됨
//							.orTimeout(3, TimeUnit.SECONDS);
//			priceFutures.add(futurePriceInUSD);
//		}

		// 타임아웃이 발생하면 기본값으로 처리
		// completeOnTimeout 메서드 사용
		for (Shop shop : shops) {
			CompletableFuture<Double> futurePriceInUSD =
					CompletableFuture.supplyAsync(() -> shop.getPrice(product))
							.thenCombine(CompletableFuture.supplyAsync(
									() -> ExchangeService.getRate(Money.EUR, Money.USD))
											// 환전 서비스가 1초 안에 결과를 제공하지 않으면 기본 환율값 사용
											.completeOnTimeout(DEFAULT_RATE, 1, TimeUnit.SECONDS),
									(price, rate) -> price * rate)
							// Client에서 이 메서드를 개별 호출(실행)할 땐 3초로 예외 발생하지 않으나
							// 다른 메서드들과 한 번에 다같이 호출시 3초 예외 발생
//							.orTimeout(3, TimeUnit.SECONDS);
							.orTimeout(10, TimeUnit.SECONDS);
			priceFutures.add(futurePriceInUSD);
		}

		// 루프 밖에서 shop에 접근할 수 없으므로 아래 getName() 호출을 주석 처리
		List<String> prices = priceFutures.stream()
				.map(CompletableFuture::join)
				.map(price -> /*shop.getName() +*/ " price is " + price)
				.collect(Collectors.toList());
		return prices;
	}
}
