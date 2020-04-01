package com.jaenyeong.chapter_03;

import java.util.function.DoubleFunction;
import java.util.function.DoubleUnaryOperator;

public class Integral {
	// f(x) = x + 10 식이 있다고 가정

	// step1 - 자바 코드를 수학 함수처럼 구현할 수 없음
//	public double integrate((double -> double) f, double a, double b) {
//		return (f(a) + f(b)) * (b - a) / 2.0
//	}

	// step2
	public double integrate(DoubleFunction<Double> f, double a, double b) {
		return (f.apply(a) + f.apply(b)) * (b - a) / 2.0;
	}

	// step3
	public double integrate(DoubleUnaryOperator f, double a, double b) {
		return (f.applyAsDouble(a) + f.applyAsDouble(b)) * (b - a) / 2.0;
	}
}
