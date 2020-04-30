package com.jaenyeong.chapter_10.DSLPatternExample.Builder;

import com.jaenyeong.chapter_10.DSLPatternExample.Order;
import com.jaenyeong.chapter_10.DSLPatternExample.Stock;
import com.jaenyeong.chapter_10.DSLPatternExample.Trade;

import java.util.function.Consumer;

public class LambdaOrderBuilder {
	// 빌더로 주문을 감싸기
	private Order order = new Order();

	public static Order order(Consumer<LambdaOrderBuilder> consumer) {
		LambdaOrderBuilder builder = new LambdaOrderBuilder();
		consumer.accept(builder); // 주문 빌더로 전달된 람다 표현식 실행
		return builder.order; // OrderBuilder의 Consumer를 실행해 만들어진 주문을 반환
	}

	public void forCustomer(String customer) {
		// 주문을 요청한 고객 설정
		order.setCustomer(customer);
	}

	public void buy(Consumer<TradeBuilder> consumer) {
		// 주식 매수 주문을 만들도록 TradeBuilder 소비
		trade(consumer, Trade.Type.BUY);
	}

	public void sell(Consumer<TradeBuilder> consumer) {
		// 주식 매도 주문을 만들도록 TradeBuilder 소비
		trade(consumer, Trade.Type.SELL);
	}

	private void trade(Consumer<TradeBuilder> consumer, Trade.Type type) {
		TradeBuilder builder = new TradeBuilder();
		builder.trade.setType(type);
		// TradeBuilder로 전달할 람다 표현식 실행
		consumer.accept(builder);
		// TradeBuilder의 Consumer를 실행해 만든 거래를 주문에 추가
		order.addTrade(builder.trade);
	}

	public static class TradeBuilder {
		private Trade trade = new Trade();

		public void quantity(int quantity) {
			trade.setQuantity(quantity);
		}

		public void price(double price) {
			trade.setPrice(price);
		}

		public void stock(Consumer<StockBuilder> consumer) {
			StockBuilder builder = new StockBuilder();
			consumer.accept(builder);
			trade.setStock(builder.stock);
		}
	}

	public static class StockBuilder {
		private Stock stock = new Stock();

		public void symbol(String symbol) {
			stock.setSymbol(symbol);
		}

		public void market(String market) {
			stock.setMarket(market);
		}
	}
}
