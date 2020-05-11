package com.jaenyeong.chapter_15.FlowExample;

public interface Publisher<T> {
	void subscribe(Subscriber<? super T> subscriber);
}
