package com.jaenyeong.chapter_17.FlowExample;

import java.util.Random;

// 현재 보고된 온도를 전달하는 자바 빈
public class TempInfo {
	public static final Random random = new Random();
	private final String town;
	private final int temp;

	public TempInfo(String town, int temp) {
		this.town = town;
		this.temp = temp;
	}

	// 정적 팩토리 메서드를 이용해 해당 도시의 TempInfo 인스턴스 생성
	public static TempInfo fetch(String town) {
		// 10분의 1 확률로 온도 가져오기 작업 실패
		if (random.nextInt(10) == 0) {
			throw new RuntimeException("Error");
		}

		// 0에서 99 사이에서 임의의 화씨 온도를 반환
		return new TempInfo(town, random.nextInt(100));
	}

	@Override
	public String toString() {
		return "TempInfo{" +
				"town='" + town + '\'' +
				", temp=" + temp +
				'}';
	}

	public String getTown() {
		return town;
	}

	public int getTemp() {
		return temp;
	}
}
