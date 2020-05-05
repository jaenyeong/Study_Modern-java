package com.jaenyeong.chapter_13.ExampleAPI;

public interface Sized {
	int size();
	default boolean isEmpty() {
		return size() == 0;
	}
}
