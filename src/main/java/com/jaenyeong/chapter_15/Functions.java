package com.jaenyeong.chapter_15;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.function.IntConsumer;

public class Functions {

	public static int f(int x) {
		return x * 2;
	}

	public static int g(int x) {
		return x + 1;
	}

	public static Future<Integer> fFuture(int x) {
//		return new CompletableFuture<Integer>().completeAsync(() -> Integer.valueOf(x * 2));
		return new CompletableFuture<Integer>().completeAsync(() -> x * 2);
	}

	public static Future<Integer> gFuture(int x) {
//		return new CompletableFuture<Integer>().completeAsync(() -> Integer.valueOf(x + 1));
		return new CompletableFuture<Integer>().completeAsync(() -> x + 1);
	}

	public static void f(int x, IntConsumer dealWithResult) {
		dealWithResult.accept(f(x));
	}

	public static void g(int x, IntConsumer dealWithResult) {
		dealWithResult.accept(g(x));
	}
}
