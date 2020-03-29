package com.jaenyeong.chapter_02.quiz;

import java.util.Arrays;
import java.util.List;

public class AppleFormatterExample {

	static class Apple {
		private int weight = 0;
		private String color = "";

		public Apple(int weight, String color) {
			this.weight = weight;
			this.color = color;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		@SuppressWarnings("boxing")
		@Override
		public String toString() {
			return String.format("Apple{color='%s', weight=%d}", color, weight);
		}
	}

	static void prettyPrintApple(List<Apple> inventory, AppleFormatter formatter) {
		for (Apple apple : inventory) {
			String output = formatter.accept(apple);
			System.out.println(output);
		}
	}

	interface AppleFormatter {
		String accept(Apple apple);
	}

	static class AppleFancyFormatter implements AppleFormatter {

		@Override
		public String accept(Apple apple) {
			String characteristic = apple.getWeight() > 150 ? "heavy" : "light";
			return "A " + characteristic + " " + apple.getColor() + " apple";
		}
	}

	static class AppleSimpleFormatter implements AppleFormatter {

		@Override
		public String accept(Apple apple) {
			return "An apple of " + apple.getWeight() + "g";
		}
	}

	public static void main(String[] args) {
		List<Apple> inventory = Arrays.asList(
				new Apple(80, "green"),
				new Apple(155, "blue"),
				new Apple(120, "red")
		);

		prettyPrintApple(inventory, new AppleFancyFormatter());
		prettyPrintApple(inventory, new AppleSimpleFormatter());
	}
}
