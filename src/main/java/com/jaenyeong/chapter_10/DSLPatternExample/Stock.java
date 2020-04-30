package com.jaenyeong.chapter_10.DSLPatternExample;

// [첫 번째] 주어진 시장에 주식 가격을 모델링하는 자바 빈즈
public class Stock {
	private String symbol;
	private String market;

	public String getSymbol() {
		return symbol;
	}

	public Stock setSymbol(String symbol) {
		this.symbol = symbol;
		return this;
	}

	public String getMarket() {
		return market;
	}

	public Stock setMarket(String market) {
		this.market = market;
		return this;
	}

	@Override
	public String toString() {
		return String.format("Stock[symbol=%s, market=%s]", symbol, market);
	}
}
