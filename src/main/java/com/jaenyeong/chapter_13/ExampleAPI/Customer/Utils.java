package com.jaenyeong.chapter_13.ExampleAPI.Customer;

import com.jaenyeong.chapter_13.ExampleAPI.Developer.Resizable;

import java.util.List;

public class Utils {

	public static void paint(List<Resizable> l) {
		l.forEach(r -> {
			r.setAbsoluteSize(42, 42);
			r.draw();
		});

		// Resizable에 추가 메서드를 사용시
//		l.forEach(r -> {
//			r.setRelativeSize(2, 2);
//		});
	}
}
