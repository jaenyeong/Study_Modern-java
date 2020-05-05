package com.jaenyeong.chapter_13.ExampleExtendsProblem;

public class D implements A {

	// 주석을 풀어 해당 메서드 오버라이드 하면 C 클래스에서 new C().hello() 호출시 'Hello from D'를 출력함
	// 하지만 주석처리하면 C 클래스에서 new C().hello() 호출시 B 인터페이스의 'Hello from B'를 출력함
//	public void hello() {
//		System.out.println("Hello from D");
//	}
}
