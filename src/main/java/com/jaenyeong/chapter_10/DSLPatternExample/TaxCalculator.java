package com.jaenyeong.chapter_10.DSLPatternExample;

import java.util.function.DoubleUnaryOperator;

import static com.jaenyeong.chapter_10.DSLPatternExample.Builder.MixedBuilder.*;

// 적용할 세금을 유창하게 정의하는 세금 계산기
public class TaxCalculator {

	public static void main(String[] args) {
		Order order =
				mixedForCustomer("BigBank",
						mixedBuy(t -> t.quantity(80)
								.stock("IBM")
								.on("NYSE")
								.at(125.00)),
						mixedSell(t -> t.quantity(50)
								.stock("GOOGLE")
								.on("NASDAQ")
								.at(125.00)));

		// 지역 세금과 추가 요금을 적용하고 일반 세금은 뺀 주문의 최종값을 계산할 수 있음
		double value = TaxCalculator.calculate(order, true, false, true);
		System.out.printf("Boolean arguments: %.2f%n", value);

		value = new TaxCalculator().withTaxRegional()
				.withTaxSurcharge()
				.calculate(order);
		System.out.printf("Method chaining: %.2f%n", value);

		value = new TaxCalculator().with(Tax::regional)
				.with(Tax::surcharge)
				.calculateF(order);
		System.out.printf("Method references: %.2f%n", value);
	}

	private boolean useRegional;
	private boolean useGeneral;
	private boolean useSurcharge;

	// 불리언 플래그 집합을 이용해 주문에 세금 적용
	public static double calculate(Order order, boolean useRegional, boolean useGeneral, boolean useSurcharge) {
		double value = order.getValue();

		if (useRegional) {
			value = Tax.regional(value);
		}
		if (useGeneral) {
			value = Tax.general(value);
		}
		if (useSurcharge) {
			value = Tax.surcharge(value);
		}
		return value;
	}

	public TaxCalculator withTaxRegional() {
		useRegional = true;
		return this;
	}

	public TaxCalculator withTaxGeneral() {
		useGeneral = true;
		return this;
	}

	public TaxCalculator withTaxSurcharge() {
		useSurcharge = true;
		return this;
	}

	public double calculate(Order order) {
		return calculate(order, useRegional, useGeneral, useSurcharge);
	}

	/**
	 * 유창하게 세금 함수를 적용하는 세금 계산기
	 * 위 TaxCalculator 내용을 리팩토링
	 */

	// 주문값에 적용된 모든 세금을 계산하는 함수
	public DoubleUnaryOperator taxFunction = d -> d;

	// 새로운 세금 계산 함수를 얻어서 인수로 전달된 함수와 현재 함수를 합침
	public TaxCalculator with(DoubleUnaryOperator f) {
		taxFunction = taxFunction.andThen(f);
		return this;
	}

	// 주문의 총 합에 세금 계산 함수를 적용해 최종 주문값을 계산
	public double calculateF(Order order) {
		return taxFunction.applyAsDouble(order.getValue());
	}
}
