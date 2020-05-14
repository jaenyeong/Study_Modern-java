package com.jaenyeong.chapter_16.OnlineStore.DiscountCode;

import static com.jaenyeong.chapter_16.OnlineStore.Util.delay;
import static com.jaenyeong.chapter_16.OnlineStore.Util.format;

public class Discount {
	public enum Code {
		NONE(0), SILVER(5), GOLD(10), PLATINUM(15), DIAMON(20);

		private final int percentage;

		Code(int percentage) {
			this.percentage = percentage;
		}
	}

	public static String applyDiscount(Quote quote) {
		return quote.getShopName() + " price is " +
				// 기존 가격에 할인 코드를 적용
				Discount.apply(quote.getPrice(), quote.getDiscountCode());
	}

	private static double apply(double price, Code code) {
		// Disocunt 서비스의 응답 지연을 흉내
		delay();
		return format(price * (100 - code.percentage) / 100);
	}
}
