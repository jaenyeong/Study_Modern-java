package com.jaenyeong.chapter_16.OnlineStore.Origin;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

import static com.jaenyeong.chapter_16.OnlineStore.Util.delay;

public class Shop {
	private String name;
	private Random random;

	public Shop(String name) {
		this.name = name;
		this.random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}

	// 기본
	public double getPrice(String product) {
		return calculatePrice(product);
	}

	// 비동기 메서드
	public Future<Double> getPriceAsyncV1(String product) {
		// 계산 결과를 포함할 CompletableFuture 생성
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();

		new Thread(() -> {
			// 다른 스레드에서 비동기적으로 계산을 수행
			double price = calculatePrice(product);
			// 오랜 시간이 걸리는 계산이 완료되면 Future에 값을 설정
			futurePrice.complete(price);
		}).start();

		// 계산 결과가 완료되길 기다리지 않고 Future를 반환
		return futurePrice;
	}

	// completeExceptionally 메서드를 이용하여 CompletableFuture 내부 예외 전달
	public Future<Double> getPriceAsyncV2(String product) {
		CompletableFuture<Double> futurePrice = new CompletableFuture<>();

		new Thread(() -> {

			try {
				double price = exceptionCalculatePrice(product);
				// 계산이 정상적으로 종료되면 Future에 가격 정보를 저장한채로 Future를 종료
				futurePrice.complete(price);
			} catch (Exception ex) {
				// 도중에 문제가 발생하면 발생한 에러를 포함시켜 Future 종료
				futurePrice.completeExceptionally(ex);
			}
		}).start();

		return futurePrice;
	}

	// 팩토리 메서드 supplyAsync로 CompletableFuture 생성
	public Future<Double> getPriceAsyncFromFactory(String product) {
		return CompletableFuture.supplyAsync(() -> calculatePrice(product));
	}

	private double calculatePrice(String product) {
		delay();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}

	private double exceptionCalculatePrice(String product) {
		delay();
		throw new RuntimeException("product not available");
//		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public static void doSomethingElse() {
		System.out.println("Doing something else...");
	}

	public String getName() {
		return this.name;
	}
}
