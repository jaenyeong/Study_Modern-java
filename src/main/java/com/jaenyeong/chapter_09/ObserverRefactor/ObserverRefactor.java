package com.jaenyeong.chapter_09.ObserverRefactor;

import java.util.ArrayList;
import java.util.List;

public class ObserverRefactor {

	public static void main(String[] args) {
		Feed f = new Feed();
//		f.registerObserver(new NYTimes());
//		f.registerObserver(new Guardian());
//		f.registerObserver(new LeMonde());
//		f.notifyObservers("The queen said her favourite book is Modern Java in Action!");

		f.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		});

		f.registerObserver((String tweet) -> {
			if (tweet != null && tweet.contains("queen")) {
				System.out.println("Yet more news from London... " + tweet);
			}
		});

		f.notifyObservers("The queen said her favourite book is Modern Java in Action!");
	}

	interface Observer {
		void notify(String tweet);
	}

	static class NYTimes implements Observer {

		@Override
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("money")) {
				System.out.println("Breaking news in NY! " + tweet);
			}
		}
	}

	static class Guardian implements Observer {

		@Override
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("queen")) {
				System.out.println("Yet more news from London... " + tweet);
			}
		}
	}

	static class LeMonde implements Observer {

		@Override
		public void notify(String tweet) {
			if (tweet != null && tweet.contains("wine")) {
				System.out.println("Today cheese, wine and news! " + tweet);
			}
		}
	}

	interface Subject {
		void registerObserver(Observer o);

		void notifyObservers(String tweet);
	}

	static class Feed implements Subject {
		private final List<Observer> observers = new ArrayList<>();

		@Override
		public void registerObserver(Observer o) {
			observers.add(o);
		}

		@Override
		public void notifyObservers(String tweet) {
			observers.forEach(o -> o.notify(tweet));
		}
	}
}
