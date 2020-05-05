package com.jaenyeong.chapter_13.Game;

public interface Resizable {

	int getWidth();

	int getHeight();

	void setWidth(int width);

	void setHeight(int height);

	void setAbsoluteSize(int width, int height);

	// setRelativeSize를 Default 메서드로 제공
	default void setRelativeSize(int wFactor, int hFactor) {
		setAbsoluteSize(getWidth() / wFactor, getHeight() / hFactor);
	}
}
