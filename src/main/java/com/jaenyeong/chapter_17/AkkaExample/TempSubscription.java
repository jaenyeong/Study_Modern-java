package com.jaenyeong.chapter_17.AkkaExample;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Flow;

// Subscriber에게 TempInfo 스트림을 전송하는 Subscription
public class TempSubscription implements Flow.Subscription {
	private final Flow.Subscriber<? super TempInfo> subscriber;
	private final String town;

	// 스택 오버 플로우 방지를 위해 Executor 추가
	private static final ExecutorService executor = Executors.newSingleThreadExecutor();

	public TempSubscription(Flow.Subscriber<? super TempInfo> subscriber, String town) {
		this.subscriber = subscriber;
		this.town = town;
	}

	@Override
	public void request(long n) {
		// Subscriber가 만든 요청을 한 개씩 반복
//		for (long i = 0L; i < n; i++) {
//			try {
//				// 현재 온도를 Subscriber로 전달
//				subscriber.onNext(TempInfo.fetch(town));
//			} catch (Exception e) {
//				// 온도 가져오기를 실패하면 Subscriber로 에러를 전달
//				subscriber.onError(e);
//				break;
//			}
//		}

		// 스택 오버 플로우 방지를 위해 Executor 추가
		executor.submit(() -> {
			for (long i = 0L; i < n; i++) {
				try {
					subscriber.onNext(TempInfo.fetch(town));
				} catch (Exception e) {
					subscriber.onError(e);
					break;
				}
			}
		});
	}

	@Override
	public void cancel() {
		// 구독이 취소되면 완료(onComplete) 신호를 Subscriber로 전달
		subscriber.onComplete();
	}
}
