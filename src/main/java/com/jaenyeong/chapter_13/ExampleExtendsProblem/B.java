package com.jaenyeong.chapter_13.ExampleExtendsProblem;

public interface B extends A {

	default void hello() {
		System.out.println("Hello from B");
	}

	// hello()를 추상 메서드로 선언시 상속 받는 인터페이스나 구현하는 클래스에서 오버라이딩을 해야함
//	void hello();
}
