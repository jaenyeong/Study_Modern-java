package com.jaenyeong.chapter_13.ExampleAPI.Developer;

public interface Resizable extends Drawable {

	int getWidth();

	int getHeight();

	void setWidth(int width);

	void setHeight(int height);

	void setAbsoluteSize(int width, int height);

	// API 버전 2에 추가된 새로운 메서드
//	void setRelativeSize(int widthFactor, int heightFactor);

	// setRelativeSize를 Default 메서드로 제공
	default void setRelativeSize(int wFactor, int hFactor) {
		setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
	}
}
