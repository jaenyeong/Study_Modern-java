package com.jaenyeong.chapter_09.LambdaTesting;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.*;

public class Point {
	// 람다 테스트를 위한 객체
	public final static Comparator<Point> compareByXAndThenY = comparing(Point::getX).thenComparing(Point::getY);

	private final int x;
	private final int y;

	public Point(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Point moveRightBy(int x) {
		return new Point(this.x, this.y);
	}

	public static List<Point> moveAllPointsRightBy(List<Point> points, int x) {
		return points.stream().map(p -> new Point(p.getX(), p.getY())).collect(toList());
	}
}
