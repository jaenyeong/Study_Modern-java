package com.jaenyeong.chapter_16.OnlineStore.Refactoring;

public class Client {
	private static final BestPriceFinder bestPriceFinder = new BestPriceFinder();

	public static void main(String[] args) {
		bestPriceFinder.printPricesStream("myPhone27S");
	}
}
