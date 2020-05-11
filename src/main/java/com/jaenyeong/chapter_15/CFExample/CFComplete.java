package com.jaenyeong.chapter_15.CFExample;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static com.jaenyeong.chapter_15.Functions.f;
import static com.jaenyeong.chapter_15.Functions.g;

public class CFComplete {

	public static void main(String[] args) throws ExecutionException, InterruptedException {
		example1();
		// 또는
		example2();
	}

	private static void example1() throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		int x = 1337;

		CompletableFuture<Integer> a = new CompletableFuture<>();
		executorService.submit(() -> a.complete(f(x)));

		int b = g(x);
		System.out.println(a.get() + b);

		executorService.shutdown();
	}

	private static void example2() throws ExecutionException, InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		int x = 1337;

		CompletableFuture<Integer> b = new CompletableFuture<>();
		executorService.submit(() -> b.complete(g(x)));

		int a = f(x);
		System.out.println(a + b.get());

		executorService.shutdown();
	}
}
