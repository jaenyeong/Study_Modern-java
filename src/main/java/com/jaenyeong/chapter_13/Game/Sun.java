package com.jaenyeong.chapter_13.Game;

public class Sun implements Moveable, Rotatable {
	// 모든 추상메서드의 구현은 제공해야 하지만 디폴트 메서드의 구현은 제공할 필요가 없음

	public static void main(String[] args) {
		Sun s = new Sun(); // 생성자는 내부적으로 좌표, 높이, 너비, 기본 각도를 설정함
		s.rotateBy(180); // Rotatable의 rotateBy 호출
		s.moveVertically(10); // Moveable의 moveVertically 호출
	}

	@Override
	public int getX() {
		return 0;
	}

	@Override
	public int getY() {
		return 0;
	}

	@Override
	public void setX(int x) {

	}

	@Override
	public void setY(int y) {

	}

	@Override
	public void setRotationAngle(int angleInDegrees) {

	}

	@Override
	public int getRotationAngle() {
		return 0;
	}
}
