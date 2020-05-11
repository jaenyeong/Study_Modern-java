package com.jaenyeong.chapter_15.FlowExample;

import java.util.ArrayList;
import java.util.List;

public class SimpleCell implements Publisher<Integer>, Subscriber<Integer> {
	private int value = 0;
	private String name;
	private List<Subscriber> subscribers = new ArrayList<>();

	public SimpleCell(String name) {
		this.name = name;
	}

	public static void main(String[] args) {
		SimpleCell c3 = new SimpleCell("C3");
		SimpleCell c2 = new SimpleCell("C2");
		SimpleCell c1 = new SimpleCell("C1");

		c1.subscribe(c3);

		c1.onNext(10); // C1의 값을 10으로 갱신
		c2.onNext(20); // C2의 값을 20으로 갱신
	}

	@Override
	public void subscribe(Subscriber<? super Integer> subscriber) {
		subscribers.add(subscriber);
	}

	@Override
	public void onNext(Integer newValue) {
		// 구독한 셀에 새 값이 생겼을 때 값을 갱신해서 반응함
		this.value = newValue;
		// 값을 콘솔로 출력하지만 실제로는 UI의 셀을 갱신할 수 있음
		System.out.println(this.name + ":" + this.value);
		// 값이 갱신되었음을 모든 구독자에게 알림
		notifyAllSubscribers();
	}

	private void notifyAllSubscribers() {
		// 새로운 값이 있음을 모든 구독자에게 알리는 메서드
		subscribers.forEach(subscriber -> subscriber.onNext(this.value));
	}
}
