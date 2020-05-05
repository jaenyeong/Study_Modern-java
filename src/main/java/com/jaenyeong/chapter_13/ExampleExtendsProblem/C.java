package com.jaenyeong.chapter_13.ExampleExtendsProblem;

// 아래와 같이 인터페이스 A를 구현한 추상 클래스 E를 상속한 경우 C 클래스에서는 hello 메서드를 오버라이드 해야함
//public class C extends E implements B, A {

// 아래와 같이 인터페이스 간 상속관계가 없는 경우 C 클래스에서 직접 명시해야 함
//public class C implements A, F {

public class C extends D implements B, A {

	public static void main(String[] args) {
		new C().hello();
	}

	// hello 메서드 직접 명시
//	public void hello() {
//		F.super.hello();
//	}
}
