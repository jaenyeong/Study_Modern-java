package com.jaenyeong.chapter_15.APIExample;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceExample {

	public static void main(String[] args) {
		ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);

		work1();

		// work1() 끝난 후 10초 뒤에 work2()를 개별 태스크로 스케줄함
		scheduledExecutorService.schedule(ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS);

		scheduledExecutorService.shutdown();
	}

	public static void work1() {
		System.out.println("Hello from Work1");
	}

	public static void work2() {
		System.out.println("Hello from Work2");
	}
}
