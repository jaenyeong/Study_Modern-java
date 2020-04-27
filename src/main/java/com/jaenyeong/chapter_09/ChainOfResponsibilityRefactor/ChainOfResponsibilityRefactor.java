package com.jaenyeong.chapter_09.ChainOfResponsibilityRefactor;

import java.util.function.Function;
import java.util.function.UnaryOperator;

public class ChainOfResponsibilityRefactor {

	public static void main(String[] args) {
		ProcessingObject<String> p1 = new HeaderTextProcessing();
		ProcessingObject<String> p2 = new SpellCheckerProcessing();

		p1.setSuccessor(p2);
		String result = p1.handle("Aren't labdas really sexy?!");
		System.out.println(result);

		// 첫 번째 작업 처리 객체
		UnaryOperator<String> headerProcessing =
				(String text) -> "From Raoul, Mario and Alan: " + text;
		// 두 번째 작업 처리 객체
		UnaryOperator<String> spellCheckerProcessing =
				(String text) -> text.replaceAll("labda", "lambda");
		// 동작 체인으로 두 함수를 조합
		Function<String, String> pipeLine =
				headerProcessing.andThen(spellCheckerProcessing);

		String result2 = pipeLine.apply("Aren't labdas really sexy?!");
	}

	public static abstract class ProcessingObject<T> {
		protected ProcessingObject<T> successor;

		public void setSuccessor(ProcessingObject<T> successor) {
			this.successor = successor;
		}

		public T handle(T input) {
			T r = handleWork(input);
			if (successor != null) {
				return successor.handle(r);
			}
			return r;
		}

		abstract protected T handleWork(T input);
	}

	public static class HeaderTextProcessing extends ProcessingObject<String> {

		@Override
		protected String handleWork(String text) {
			return "From Raoul, Mario and Alan: " + text;
		}
	}

	public static class SpellCheckerProcessing extends ProcessingObject<String> {

		@Override
		protected String handleWork(String text) {
			return text.replaceAll("labda", "lambda");
		}
	}
}
