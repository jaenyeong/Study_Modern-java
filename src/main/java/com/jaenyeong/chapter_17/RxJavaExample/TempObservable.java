package com.jaenyeong.chapter_17.RxJavaExample;

import io.reactivex.Observable;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static java.util.stream.Collectors.toList;

// 1초마다 한 개의 온도를 방출하는 Observable
public class TempObservable {

	public static Observable<TempInfo> getTemperature(String town) {
		// Observer를 소비하는 함수로부터 Observable 만들기
		return Observable.create(
				// 매 초마다 무한으로 증가하는 일련의 long값을 방출하는 Observable
				emitter -> Observable.interval(1, TimeUnit.SECONDS)
						.subscribe(i -> {
							// 소비된 옵저버가 아직 폐기되지 않았으면 어떤 작업을 수행 (이전 에러)
							if (!emitter.isDisposed()) {
								// 온도를 다섯 번 보고했으면 옵저버를 완료하고 스트림을 종료
								if (i >= 5) {
									emitter.onComplete();
								} else {
									try {
										// 아니면 온도를 Observer로 보고
										emitter.onNext(TempInfo.fetch(town));
									} catch (Exception e) {
										// 에러가 발생하면 Observer에 알림
										emitter.onError(e);
									}
								}
							}
						}));
	}

	// 화씨 온도를 섭씨 온도로 변환
	public static Observable<TempInfo> getCelsiusTemperature(String town) {
		return getTemperature(town)
				.map(temp -> new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 / 9));
	}

	// 영하 온도만 추출
	public static Observable<TempInfo> getNegativeTemperature(String town) {
		return getCelsiusTemperature(town)
				.filter(temp -> temp.getTemp() < 0);
	}

	// 한 개 이상 도시의 온도 보고를 합침
	public static Observable<TempInfo> getCelsiusTemperatures(String... towns) {
		return Observable.merge(Arrays.stream(towns)
				.map(TempObservable::getCelsiusTemperature)
				.collect(toList()));
	}
}
