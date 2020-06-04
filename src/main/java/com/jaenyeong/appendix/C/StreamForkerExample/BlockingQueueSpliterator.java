package com.jaenyeong.appendix.C.StreamForkerExample;

import java.util.Spliterator;
import java.util.concurrent.BlockingQueue;
import java.util.function.Consumer;

import static com.jaenyeong.appendix.C.StreamForkerExample.StreamForker.*;

// BlockingQueue를 탐색하면서 요소를 읽는 Spliterator
class BlockingQueueSpliterator<T> implements Spliterator<T> {
	private final BlockingQueue<T> q;

	public BlockingQueueSpliterator(BlockingQueue<T> q) {
		this.q = q;
	}

	@Override
	public boolean tryAdvance(Consumer<? super T> action) {
		T t;
		while (true) {
			try {
				t = q.take();
				break;
			} catch (InterruptedException e) {
//				e.printStackTrace();
			}
		}

		if (t != ForkingStreamConsumer.END_OF_STREAM) {
			action.accept(t);
			return true;
		}

		return false;
	}

	@Override
	public Spliterator<T> trySplit() {
		return null;
	}

	@Override
	public long estimateSize() {
		return 0;
	}

	@Override
	public int characteristics() {
		return 0;
	}
}
