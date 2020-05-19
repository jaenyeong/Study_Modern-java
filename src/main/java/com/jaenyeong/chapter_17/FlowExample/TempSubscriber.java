package com.jaenyeong.chapter_17.FlowExample;

import java.util.concurrent.Flow;

// 받은 온도를 출력하는 Subscriber
public class TempSubscriber implements Flow.Subscriber<TempInfo> {
	private Flow.Subscription subscription;

	// 구독을 저장하고 첫 번째 요청을 전달
	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		this.subscription = subscription;
		subscription.request(1);
	}

	// 수신한 온도를 출력하고 다음 정보를 요청
	@Override
	public void onNext(TempInfo tempInfo) {
		System.out.println(tempInfo);
		subscription.request(1);
	}

	// 에러가 발생하면 에러 메시지 출력
	@Override
	public void onError(Throwable throwable) {
		System.err.println(throwable.getMessage());
	}

	@Override
	public void onComplete() {
		System.out.println("DONE!");
	}
}
