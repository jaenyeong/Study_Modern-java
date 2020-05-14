package com.jaenyeong.chapter_16.Version_01;

import java.util.concurrent.*;

public class Example {

	public static void main(String[] args) {
		// 스레드 풀에 태스크를 제출하려면 ExecutorService를 만들어야 함
		ExecutorService executor = Executors.newCachedThreadPool();

		// Callable을 ExecutorService로 제출
		Future<Double> future = executor.submit(new Callable<Double>() {
			@Override
			public Double call() throws Exception {
				return doSomeLongComputation();
			}
		});

		// 비동기 작업을 수행하는 동안 다른 스레드에서 비동기적으로 실행
		doSomethingElse();

		try {
			// 비동기 작업의 결과를 가져옴
			// 결과가 준비되어 있지 않으면 호출 스레드가 블록됨
			// 하지만 최대 1초까지 기다림
			Double result = future.get(1, TimeUnit.SECONDS);
			System.out.println("result: " + result);
		} catch (ExecutionException ee) {
			// 계산 중 예외 발생
			ee.printStackTrace();
		} catch (InterruptedException ie) {
			// 현재 스레드에서 대기 중 인터럽트 발생
			ie.printStackTrace();
		} catch (TimeoutException te) {
			// Future가 완료되기 전에 타임아웃 발생
			te.printStackTrace();
		} finally {
			executor.shutdown();
		}
	}

	private static Double doSomeLongComputation() throws InterruptedException {
		Thread.sleep(1000);
//		Thread.sleep(2000);
		return 1d;
	}

	private static void doSomethingElse() {
		System.out.println("Doing something else...");
	}
}
