package com.jaenyeong.chapter_15.APIExample;

import static com.jaenyeong.chapter_15.Functions.f;
import static com.jaenyeong.chapter_15.Functions.g;

public class CallbackStyleExample {

	public static void main(String[] args) {
		int x = 1337;
		Result result = new Result();

		f(x, (int y) -> {
			result.left = y;
			System.out.println(result.left + result.right);
		});

		g(x, (int z) -> {
			result.right = z;
			System.out.println(result.left + result.right);
		});
	}

	private static class Result {
		private int left;
		private int right;
	}
}
