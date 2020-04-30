package com.jaenyeong.chapter_10.JOOQ;

import com.jaenyeong.chapter_10.DSLPatternExample.Order;
import com.jaenyeong.chapter_10.DSLPatternExample.Stock;
import com.jaenyeong.chapter_10.DSLPatternExample.Trade;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class BuyStocksStepsTest {
	private Map<String, Integer> stockUnitPrices = new HashMap<>();
	private Order order = new Order();

	// 시나리오의 전제 조건인 주식 단가 정의
	@Given("^the price of a \"(.*?)\" stock is (\\d+)\\$$")
	public void setUnitPrice(String stockName, int unitPrice) {
		stockUnitPrices.put(stockName, unitPrice); // 주식 단가 저장
	}

	// 테스트 대상인 도메인 모델의 행할 액션 정의
	@When("^I buy (\\d+) \"(.*?)\"$")
	public void buyStocks(int quantity, String stockName) {
		Trade trade = new Trade();
		trade.setType(Trade.Type.BUY);

		Stock stock = new Stock();
		stock.setSymbol(stockName);

		trade.setStock(stock);
		trade.setPrice(stockUnitPrices.get(stockName));
		trade.setQuantity(quantity);
		order.addTrade(trade);
	}

	// 예상되는 시나리오 결과 정의
	@Then("^the order value should be (\\d+)\\$$")
	public void checkOrderValue(int expectedValue) {
		assertEquals(expectedValue, order.getValue());
	}
}
