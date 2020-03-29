package com.jaenyeong.chapter_02.quiz;

public class MeaningOfThis {
	public final int value = 4;

	public void doIt() {
		int value = 6;
		Runnable r = new Runnable() {
			public final int value = 5;

			@Override
			public void run() {
				int value = 10;
				System.out.println(this.value);
			}
		};
		r.run();
	}

	// 결과 예측하기
	// 출력값은 5
	public static void main(String[] args) {
		MeaningOfThis m = new MeaningOfThis();
		m.doIt();
	}
}
