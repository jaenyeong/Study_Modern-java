package com.jaenyeong.chapter_16.OnlineStore.ExchangeRate;

import java.util.Random;

import static com.jaenyeong.chapter_16.OnlineStore.Util.delay;

public class Shop {
	private String name;
	private Random random;

	public Shop(String name) {
		this.name = name;
		this.random = new Random(name.charAt(0) * name.charAt(1) * name.charAt(2));
	}

	public double getPrice(String product) {
		return calculatePrice(product);
	}

	private double calculatePrice(String product) {
		delay();
		return random.nextDouble() * product.charAt(0) + product.charAt(1);
	}

	public String getName() {
		return this.name;
	}
}
