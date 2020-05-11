package com.jaenyeong.chapter_15.FlowExample;

public class ArithmeticCell extends SimpleCell {
	private int left;
	private int right;

	public ArithmeticCell(String name) {
		super(name);
	}

	public static void main(String[] args) {
		example1();
		example2();
	}

	private static void example1() {
		ArithmeticCell c3 = new ArithmeticCell("C3");
		SimpleCell c2 = new SimpleCell("C2");
		SimpleCell c1 = new SimpleCell("C1");

		c1.subscribe(c3::setLeft);
		c2.subscribe(c3::setRight);

		c1.onNext(10); // C1의 값을 10으로 갱신
		c2.onNext(20); // C2의 값을 20으로 갱신
		c1.onNext(15); // C1의 값을 15으로 갱신
	}

	private static void example2() {
		ArithmeticCell c5 = new ArithmeticCell("C5");
		ArithmeticCell c3 = new ArithmeticCell("C3");

		SimpleCell c4 = new SimpleCell("C4");
		SimpleCell c2 = new SimpleCell("C2");
		SimpleCell c1 = new SimpleCell("C1");

		c1.subscribe(c3::setLeft);
		c2.subscribe(c3::setRight);

		c3.subscribe(c5::setLeft);
		c4.subscribe(c5::setRight);

		c1.onNext(10); // C1의 값을 10으로 갱신
		c2.onNext(20); // C2의 값을 20으로 갱신
		c1.onNext(15); // C1의 값을 15로 갱신
		c4.onNext(1); // C4의 값을 1로 갱신
		c4.onNext(3); // C4의 값을 3으로 갱신
	}

	public void setLeft(int left) {
		this.left = left;
		// 셀 값을 갱신하고 모든 구독자에 알림
		onNext(left + this.right);
	}

	public void setRight(int right) {
		this.right = right;
		// 셀 값을 갱신하고 모든 구독자에 알림
		onNext(right + this.left);
	}
}
