package com.jaenyeong.chapter_10.DSLPatternExample;

// [두 번째] 주어진 가격에서 주어진 양의 주식을 사거나 파는 거래
public class Trade {
	public enum Type {BUY, SELL}
	private Type type;

	private Stock stock;
	private int quantity;
	private double price;

	public Type getType() {
		return type;
	}

	public Trade setType(Type type) {
		this.type = type;
		return this;
	}

	public Stock getStock() {
		return stock;
	}

	public Trade setStock(Stock stock) {
		this.stock = stock;
		return this;
	}

	public int getQuantity() {
		return quantity;
	}

	public Trade setQuantity(int quantity) {
		this.quantity = quantity;
		return this;
	}

	public double getPrice() {
		return price;
	}

	public Trade setPrice(double price) {
		this.price = price;
		return this;
	}

	public double getValue() {
		return this.quantity * this.price;
	}

	@Override
	public String toString() {
		return String.format("Trade[type=%s, stock=%s, quantity=%d, price=%.2f]", type, stock, quantity, price);
	}
}
