package com.jaenyeong.chapter_19.PersistenceExample;

public class TrainJourney {
	public int price;
	public TrainJourney onward;

	public TrainJourney(int p, TrainJourney t) {
		this.price = p;
		this.onward = t;
	}

	// 기존 단순한 명령형 메서드
	static TrainJourney link(TrainJourney a, TrainJourney b) {
		if (a == null) {
			return b;
		}

		TrainJourney t = a;
		while (t.onward != null) {
			t = t.onward;
		}

		t.onward = b;

		return a;
	}

	// 함수형 메서드
	static TrainJourney append(TrainJourney a, TrainJourney b) {
		return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
	}
}
