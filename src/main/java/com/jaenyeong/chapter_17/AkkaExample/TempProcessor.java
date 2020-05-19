package com.jaenyeong.chapter_17.AkkaExample;

import java.util.concurrent.Flow;

// 화씨를 섭씨로 변환하는 Processor
// TempInfo를 다른 TempInfo로 변환하는 프로세서
public class TempProcessor implements Flow.Processor<TempInfo, TempInfo> {
	private Flow.Subscriber<? super TempInfo> subscriber;

	@Override
	public void subscribe(Flow.Subscriber<? super TempInfo> subscriber) {
		this.subscriber = subscriber;
	}

	// 다른 모든 신호는 업스트림 구독자에게 전달
	@Override
	public void onSubscribe(Flow.Subscription subscription) {
		subscriber.onSubscribe(subscription);
	}

	@Override
	public void onNext(TempInfo temp) {
		// 섭씨로 변환 후 TempInfo를 다시 전송
		subscriber.onNext(new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 / 9));
	}

	// 다른 모든 신호는 업스트림 구독자에게 전달
	@Override
	public void onError(Throwable throwable) {
		subscriber.onError(throwable);
	}

	// 다른 모든 신호는 업스트림 구독자에게 전달
	@Override
	public void onComplete() {
		subscriber.onComplete();
	}
}
