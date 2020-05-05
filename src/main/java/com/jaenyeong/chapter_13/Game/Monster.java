package com.jaenyeong.chapter_13.Game;

public class Monster implements Rotatable, Moveable, Resizable {
	// 모든 추상메서드의 구현은 제공해야 하지만 디폴트 메서드의 구현은 제공할 필요가 없음

	public static void main(String[] args) {
		Monster m = new Monster(); // 생성자는 내부적으로 좌표, 높이, 너비, 기본 각도를 설정함
		m.rotateBy(180); // Rotatable의 rotateBy 호출
		m.moveVertically(10); // Moveable의 moveVertically 호출
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
	public int getWidth() {
		return 0;
	}

	@Override
	public int getHeight() {
		return 0;
	}

	@Override
	public void setWidth(int width) {

	}

	@Override
	public void setHeight(int height) {

	}

	@Override
	public void setAbsoluteSize(int width, int height) {

	}

	@Override
	public void setRotationAngle(int angleInDegrees) {

	}

	@Override
	public int getRotationAngle() {
		return 0;
	}
}
