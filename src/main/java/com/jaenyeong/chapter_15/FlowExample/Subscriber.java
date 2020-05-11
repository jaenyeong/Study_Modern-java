package com.jaenyeong.chapter_15.FlowExample;

public interface Subscriber<T> {
	void onNext(T t);
}
