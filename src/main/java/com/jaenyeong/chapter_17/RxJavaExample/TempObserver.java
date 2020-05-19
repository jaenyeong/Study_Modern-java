package com.jaenyeong.chapter_17.RxJavaExample;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

// 수신한 온도를 출력하는 Observer
public class TempObserver implements Observer<TempInfo> {
	@Override
	public void onSubscribe(Disposable d) {
	}

	@Override
	public void onNext(TempInfo tempInfo) {
		System.out.println(tempInfo);
	}

	@Override
	public void onError(Throwable e) {
		System.out.println("Got problem: " + e.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("Done!");
	}
}
