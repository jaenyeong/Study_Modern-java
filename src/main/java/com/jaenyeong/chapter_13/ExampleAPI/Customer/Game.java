package com.jaenyeong.chapter_13.ExampleAPI.Customer;

import com.jaenyeong.chapter_13.ExampleAPI.Developer.Rectangle;
import com.jaenyeong.chapter_13.ExampleAPI.Developer.Resizable;
import com.jaenyeong.chapter_13.ExampleAPI.Developer.Square;

import java.util.Arrays;
import java.util.List;

public class Game {

	public static void main(String[] args) {
		// 크기를 조절할 수 있는 모양 리스트
		List<Resizable> resizeableShapes = Arrays.asList(new Square(), new Rectangle(), new Ellipse());
		Utils.paint(resizeableShapes);
	}
}
