package com.jaenyeong.chapter_10.DSLPatternExample.Builder;

import com.jaenyeong.chapter_10.DSLPatternExample.Order;
import com.jaenyeong.chapter_10.DSLPatternExample.Stock;
import com.jaenyeong.chapter_10.DSLPatternExample.Trade;

public class MethodChainingOrderBuilder {
	// 빌더로 감싼 주문
	public final Order order = new Order();

	// private 생성자
	private MethodChainingOrderBuilder(String customer) {
		order.setCustomer(customer);
	}

	// 고객의 주문을 만드는 정적 팩토리 메서드
	public static MethodChainingOrderBuilder forCustomer(String customer) {
		return new MethodChainingOrderBuilder(customer);
	}

	// 주식을 사는 TradeBuilder 만들기
	public TradeBuilder buy(int quantity) {
		return new TradeBuilder(this, Trade.Type.BUY, quantity);
	}

	// 주식을 파는 TradeBuilder 만들기
	public TradeBuilder sell(int quantity) {
		return new TradeBuilder(this, Trade.Type.SELL, quantity);
	}

	// 유연하게 추가 주문을 만들어 추가할 수 있도록 주문 빌더 자체를 반환
	public MethodChainingOrderBuilder addTrade(Trade trade) {
		this.order.addTrade(trade); // 주문에 주식 추가
		return this;
	}

	// 주문 만들기를 종료하고 반환
	public Order end() {
		return order;
	}

	// 빌더를 계속 이어 가려면 Stock 클래스의 인스턴스를 만드는 TradeBuilder의 공개 메서드를 이용해야 함
	public static class TradeBuilder {
		private final MethodChainingOrderBuilder builder;
		public final Trade trade = new Trade();

		public TradeBuilder(MethodChainingOrderBuilder builder, Trade.Type type, int quantity) {
			this.builder = builder;
			trade.setType(type);
			trade.setQuantity(quantity);
		}

		public StockBuilder stock(String symbol) {
			return new StockBuilder(this.builder, this.trade, symbol);
		}
	}

	// 주식의 시장을 지정하고, 거래에 주식을 추가하고, 최종 빌더를 반환하는 on() 메서드 한 개를 정의
	public static class StockBuilder {
		private final MethodChainingOrderBuilder builder;
		private final Trade trade;
		private final Stock stock = new Stock();

		private StockBuilder(MethodChainingOrderBuilder builder, Trade trade, String symbol) {
			this.builder = builder;
			this.trade = trade;
			this.stock.setSymbol(symbol);
		}

		public TraderBuilderWithStock on(String market) {
			this.stock.setMarket(market);
			this.trade.setStock(this.stock);
			return new TraderBuilderWithStock(this.builder, this.trade);
		}
	}

	public static class TraderBuilderWithStock {
		private final MethodChainingOrderBuilder builder;
		private final Trade trade;

		public TraderBuilderWithStock(MethodChainingOrderBuilder builder, Trade trade) {
			this.builder = builder;
			this.trade = trade;
		}

		public MethodChainingOrderBuilder at(double price) {
			this.trade.setPrice(price);
			return this.builder.addTrade(this.trade);
		}
	}
}
