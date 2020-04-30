package com.jaenyeong.chapter_10.DSLPatternExample.Builder;

import com.jaenyeong.chapter_10.DSLPatternExample.Order;
import com.jaenyeong.chapter_10.DSLPatternExample.Stock;
import com.jaenyeong.chapter_10.DSLPatternExample.Trade;

import java.util.function.Consumer;
import java.util.stream.Stream;

public class MixedBuilder {

	public static Order mixedForCustomer(String customer, TradeBuilder... builders) {
		Order order = new Order();
		order.setCustomer(customer);
		Stream.of(builders).forEach(b -> order.addTrade(b.trade));
		return order;
	}

	public static TradeBuilder mixedBuy(Consumer<TradeBuilder> consumer) {
		return buildTrade(consumer, Trade.Type.BUY);
	}

	public static TradeBuilder mixedSell(Consumer<TradeBuilder> consumer) {
		return buildTrade(consumer, Trade.Type.SELL);
	}

	private static TradeBuilder buildTrade(Consumer<TradeBuilder> consumer, Trade.Type buy) {
		TradeBuilder builder = new TradeBuilder();
		builder.trade.setType(buy);
		consumer.accept(builder);
		return builder;
	}

	public static class TradeBuilder {
		private Trade trade = new Trade();

		public TradeBuilder quantity(int quantity) {
			trade.setQuantity(quantity);
			return this;
		}

		public TradeBuilder at(double price) {
			trade.setPrice(price);
			return this;
		}

		public StockBuilder stock(String symbol) {
			return new StockBuilder(this, trade, symbol);
		}
	}

	public static class StockBuilder {
		private final TradeBuilder builder;
		private final Trade trade;
		private final Stock stock = new Stock();

		private StockBuilder(TradeBuilder builder, Trade trade, String symbol) {
			this.builder = builder;
			this.trade = trade;
			stock.setSymbol(symbol);
		}

		public TradeBuilder on(String market) {
			stock.setMarket(market);
			trade.setStock(stock);
			return builder;
		}
	}
}
