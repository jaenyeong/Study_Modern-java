package com.jaenyeong.chapter_21;

// 값 형식 예제
public class Complex {
	public final double re;
	public final double im;

	public Complex(double re, double im) {
		this.re = re;
		this.im = im;
	}

	public static Complex add(Complex a, Complex b) {
		return new Complex(a.re + b.re, a.im + b.im);
	}
}
