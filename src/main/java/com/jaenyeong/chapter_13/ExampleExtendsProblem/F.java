package com.jaenyeong.chapter_13.ExampleExtendsProblem;

public interface F {

	default void hello() {
		System.out.println("Hello from F");
	}
}
