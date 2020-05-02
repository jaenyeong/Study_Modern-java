package com.jaenyeong.chapter_11.OptionalVersion;

import java.util.Optional;

public class OptionalCar {
	// 자동차가 보험에 가입되어 있을 수도 아닐 수도 있으므로 Optional 정의
	private Optional<OptionalInsurance> insurance;

	public Optional<OptionalInsurance> getInsurance() {
		return insurance;
	}
}
