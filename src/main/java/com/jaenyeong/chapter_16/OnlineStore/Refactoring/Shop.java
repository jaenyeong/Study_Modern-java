package com.jaenyeong.chapter_16.OnlineStore.Refactoring;

import java.util.Random;

import static com.jaenyeong.chapter_16.OnlineStore.Util.delay;

public class Shop {
	private String name;
	private Random random;

	public Shop(String name) {
		this.name = name;
		this.random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}

	public String getPrice(String product) {
		double price = calculatePrice(product);
		Discount.Code code = Discount.Code.values()[random.nextInt(Discount.Code.values().length)];

		return String.format("%s:%.2f:%s", name, price, code);
	}

	private double calculatePrice(String product) {
		delay();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}
	
	public String getName() {
		return this.name;
	}
}
