package com.jaenyeong.chapter_07;

import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class WordCount {
	public static final String SENTENCE =
			" Nel       mezzo del cammin  di nostra  vita "
					+ "mi  ritrovai in una  selva oscura"
					+ " che la  dritta via era   smarrita ";

	public static void main(String[] args) {
		System.out.println("Found " + countWordsIteratively(SENTENCE) + " words");

		Stream<Character> stream = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt);
		System.out.println("Found " + countWords(stream) + " words");

		Stream<Character> parallelStream = IntStream.range(0, SENTENCE.length()).mapToObj(SENTENCE::charAt);
		System.out.println("Found " + countWords(parallelStream.parallel()) + " words");

		System.out.println("Found " + countWords(SENTENCE) + " words");
	}

	public static int countWordsIteratively(String s) {
		int counter = 0;
		boolean lastSpace = true;

		// 문자열의 모든 문자를 하나씩 탐색
		for (char c : s.toCharArray()) {
			if (Character.isWhitespace(c)) {
				lastSpace = true;
			} else {
				// 문자를 하나씩 탐색하다 공백 문자를 만나면 지금까지 탐색한 문자를 단어로 간주하여 (공백 문자는 제외) 단어 수를 증가시킴
				if (lastSpace) counter++;
				lastSpace = false;
			}
		}
		return counter;
	}

	private static int countWords(Stream<Character> stream) {
		WordCounter wordCounter = stream.reduce(new WordCounter(0, true),
				WordCounter::accumulate,
				WordCounter::combine);
		return wordCounter.getCounter();
	}

	public static int countWords(String s) {
		//Stream<Character> stream = IntStream.range(0, s.length())
		//    .mapToObj(SENTENCE::charAt).parallel();
		Spliterator<Character> spliterator = new WordCounterSpliterator(s);
		Stream<Character> stream = StreamSupport.stream(spliterator, true);

		return countWords(stream);
	}

	private static class WordCounter {
		private final int counter;
		private final boolean lastSpace;

		public WordCounter(int counter, boolean lastSpace) {
			this.counter = counter;
			this.lastSpace = lastSpace;
		}

		// 반복 알고리즘처럼 accumulate 메서드는 문자열의 문자를 하나씩 탐색함
		public WordCounter accumulate(Character c) {
			if (Character.isWhitespace(c)) {
				return lastSpace ? this : new WordCounter(counter, true);
			} else {
				// 문자를 하나씩 탐색하다 공백 문자를 만나면 지금까지 탐색한 문자를 단어로 간주하여 단어 수를 증가시킴
				return lastSpace ? new WordCounter(counter + 1, false) : this;
			}
		}

		public WordCounter combine(WordCounter wordCounter) {
			// 두 WordCounter의 counter 값을 더함
			// counter 값만 더할 것이므로 마지막 공백은 신경 쓰지 않음
			return new WordCounter(counter + wordCounter.counter, wordCounter.lastSpace);
		}

		public int getCounter() {
			return counter;
		}
	}

	// 단어 끝에서 문자열을 분할하는 문자 Spliterator
	private static class WordCounterSpliterator implements Spliterator<Character> {
		private final String string;
		private int currentChar = 0;

		public WordCounterSpliterator(String string) {
			this.string = string;
		}

		@Override
		public boolean tryAdvance(Consumer<? super Character> action) {
			// 현재 문자 소비
			action.accept(string.charAt(currentChar++));
			// 소비할 문자가 남아있으면 true를 반환
			return currentChar < string.length();
		}

		@Override
		public Spliterator<Character> trySplit() {
			int currentSize = string.length() - currentChar;

			if (currentSize < 10) {
				// 파싱할 문자열을 순차 처리할 수 있을 만큼 충분히 작아졌음을 알리는 null을 반환
				return null;
			}

			// 파싱할 문자열의 중간을 분할 위치로 설정
			for (int splitPos = currentSize / 2 + currentChar; splitPos < string.length(); splitPos++) {
				// 다음 공백이 나올 때까지 분할 위치를 뒤로 이동 시킴
				if (Character.isWhitespace(string.charAt(splitPos))) {
					// 처음부터 분할 위치까지 문자열을 파싱할 새로운 WordCounterSpliterator를 생성
					Spliterator<Character> spliterator = new WordCounterSpliterator(string.substring(currentChar, splitPos));
					// 이 WordCounterSpliterator의 시작 위치를 분할 위치로 설정함
					currentChar = splitPos;
					// 공백을 찾았고 문자열을 분리했으므로 루프를 종료
					return spliterator;
				}
			}

			return null;
		}

		@Override
		public long estimateSize() {
			return string.length() - currentChar;
		}

		@Override
		public int characteristics() {
			return ORDERED + SIZED + SUBSIZED + NONNULL + IMMUTABLE;
		}
	}
}
