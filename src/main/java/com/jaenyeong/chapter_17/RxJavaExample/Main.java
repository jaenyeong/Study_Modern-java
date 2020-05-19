package com.jaenyeong.chapter_17.RxJavaExample;

import io.reactivex.Observable;

import static com.jaenyeong.chapter_17.RxJavaExample.TempObservable.*;

public class Main {

	public static void main(String[] args) {
		// Runtime Error
//		Observable<Long> onePerSec = Observable.interval(1, TimeUnit.SECONDS);
//		onePerSec.blockingSubscribe(
//				i -> System.out.println(TempInfo.fetch("New York"))
//		);

//		trySleepSubscribe();
//		blockingSubscribe();

		mergeCitySubscribe();
	}

	private static void trySleepSubscribe() {
		// 매 초마다 뉴욕의 온도 보고를 방출하는 Observable 만들기
		Observable<TempInfo> observable = getTemperature("New York");
		// 단순 Observer로 이 Observable에 가입해서 온도 출력하기
		observable.subscribe(new TempObserver());

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private static void blockingSubscribe() {
		// 매 초마다 뉴욕의 온도 보고를 방출하는 Observable 만들기
		Observable<TempInfo> observable = getTemperature("New York");
		// 단순 Observer로 이 Observable에 가입해서 온도 출력하기
		observable.blockingSubscribe(new TempObserver());
	}

	private static void mergeCitySubscribe() {
		Observable<TempInfo> observable = getCelsiusTemperatures("New York", "Chicago", "San Francisco");
		observable.blockingSubscribe(new TempObserver());
	}
}
