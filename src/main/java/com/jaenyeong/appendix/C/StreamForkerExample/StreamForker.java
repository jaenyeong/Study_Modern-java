package com.jaenyeong.appendix.C.StreamForkerExample;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

// 스트림에 여러 연산을 수행하는데 필요한 StreamForker
public class StreamForker<T> {
	private final Stream<T> stream;
	private final Map<Object, Function<Stream<T>, ?>> forks = new HashMap<>();

	public StreamForker(Stream<T> stream) {
		this.stream = stream;
	}

	public StreamForker<T> fork(Object key, Function<Stream<T>, ?> f) {
		forks.put(key, f); // 스트림에 적용할 함수 저장
		return this;       // 유연하게 fork 메서드를 여러 번 호출할 수 있도록 this 반환
	}

	public Results getResults() {
		ForkingStreamConsumer<T> consumer = build();
		try {
			stream.sequential().forEach(consumer);
		} finally {
			consumer.finish();
		}
		return consumer;
	}

	// ForkingStreamConsumer를 생성하는 build 메서드
	private ForkingStreamConsumer<T> build() {
		// 각각의 연산을 저장할 큐 리스트 생성
		List<BlockingQueue<T>> queues = new ArrayList<>();

		// 연산 결과를 포함하는 Future 연산을 식별할 수 있는 키에 대응시켜 맵에 저장
		Map<Object, Future<?>> actions =
				forks.entrySet()
						.stream()
						.reduce(
//								new HashMap<Object, Future<?>>(),
								new HashMap<>(),
								(map, e) -> {
									map.put(e.getKey(),
											getOperationResult(queues, e.getValue()));
									return map;
								},
								(m1, m2) -> {
									m1.putAll(m2);
									return m1;
								});

		return new ForkingStreamConsumer<>(queues, actions);
	}

	// Future 생성 메서드
	private Future<?> getOperationResult(List<BlockingQueue<T>> queues, Function<Stream<T>, ?> f) {
		BlockingQueue<T> queue = new LinkedBlockingQueue<>();

		// 큐를 만들어 큐 리스트에 추가
		queues.add(queue);
		// 큐의 요소를 탐색하는 Spliterator
		Spliterator<T> spliterator = new BlockingQueueSpliterator<>(queue);
		// Spliterator를 소스로 갖는 스트림을 생성
		Stream<T> source = StreamSupport.stream(spliterator, false);
		// 스트림에서 주어진 함수를 비동기로 적용해서 결과를 얻을 Future 생성
		return CompletableFuture.supplyAsync(() -> f.apply(source));
	}

	/**
	 * 스트림 요소를 여러 큐로 추가하는 ForkingStreamConsumer 클래스
	 */
	static class ForkingStreamConsumer<T> implements Consumer<T>, Results {
		static final Object END_OF_STREAM = new Object();

		private final List<BlockingQueue<T>> queues;
		private final Map<Object, Future<?>> actions;

		ForkingStreamConsumer(List<BlockingQueue<T>> queues, Map<Object, Future<?>> actions) {
			this.queues = queues;
			this.actions = actions;
		}

		@Override
		public <R> R get(Object key) {
			try {
				// 키에 대응하는 동작의 결과를 반환, Future의 계산 완료 대기
				return ((Future<R>) actions.get(key)).get();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void accept(T t) {
			// 스트림에서 탐색한 요소를 모든 큐로 전달
			queues.forEach(q -> q.add(t));
		}

		void finish() {
			// 스트림의 끝을 알리는 마지막 요소를 큐에 삽입
			accept((T) END_OF_STREAM);
		}
	}
}
