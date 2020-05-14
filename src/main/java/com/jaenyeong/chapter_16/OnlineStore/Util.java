package com.jaenyeong.chapter_16.OnlineStore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Random;

public class Util {
	private static final Random RANDOM = new Random(0);
	private static final DecimalFormat formatter =
			new DecimalFormat("#.##", new DecimalFormatSymbols(Locale.US));

	public static void delay() {
		int delay = 1000;
		delay(delay);
	}

	public static void randomDelay() {
		int delay = 500 + RANDOM.nextInt(2000);
		delay(delay);
	}

	private static void delay(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}

	public static double format(double number) {
		synchronized (formatter) {
//			return new Double(formatter.format(number));
			return Double.parseDouble(formatter.format(number));
		}
	}
}
