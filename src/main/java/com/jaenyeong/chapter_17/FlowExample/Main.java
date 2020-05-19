package com.jaenyeong.chapter_17.FlowExample;

import java.util.concurrent.Flow;

// Publisher를 만들고 TempSubscriber를 구독시킴
public class Main {

	public static void main(String[] args) {
//		getTemperatures("New York").subscribe(new TempSubscriber());

		// 뉴욕의 섭씨 온도를 전송할 Publisher 생성, TempSubscriber를 Publisher로 구독
		getCelsiusTemperatures("New York").subscribe(new TempSubscriber());
	}

	// 구독한 Subscriber에게 TempSubscription을 전송하는 Publisher를 반환
	private static Flow.Publisher<TempInfo> getTemperatures(String town) {
		return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
	}

	public static Flow.Publisher<TempInfo> getCelsiusTemperatures(String town) {
		return subscriber -> {
			// TempProcessor를 만들고 Subscriber와 반환된 Publisher 사이로 연결
			TempProcessor processor = new TempProcessor();
			processor.subscribe(subscriber);
			processor.onSubscribe(new TempSubscription(processor, town));
		};
	}
}
