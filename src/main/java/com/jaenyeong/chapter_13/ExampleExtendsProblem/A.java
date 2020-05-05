package com.jaenyeong.chapter_13.ExampleExtendsProblem;

public interface A {

	default void hello() {
		System.out.println("Hello from A");
	}
}
