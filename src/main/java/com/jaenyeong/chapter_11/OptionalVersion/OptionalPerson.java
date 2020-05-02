package com.jaenyeong.chapter_11.OptionalVersion;

import java.util.Optional;

public class OptionalPerson {
	// 사람이 차를 소유했을 수도 아닐 수도 있으므로 Optional 정의
	private Optional<OptionalCar> car;
	private int age;

	public Optional<OptionalCar> getCar() {
		return car;
	}

	public int getAge() {
		return age;
	}
}
