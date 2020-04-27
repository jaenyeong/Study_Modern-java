package com.jaenyeong.chapter_09.StrategyRefactor;

public class StrategyRefactor {

	public static void main(String[] args) {
//		Validator numericValidator = new Validator(new IsNumeric());
//		boolean b1 = numericValidator.validate("aaaa"); // false 반환
//
//		Validator lowerCaseValidator = new Validator(new IsAllLowerCase());
//		boolean b2 = lowerCaseValidator.validate("bbbb"); // true 반환

		Validator numericValidator = new Validator(s -> s.matches("[a-z]+"));
		boolean b1 = numericValidator.validate("aaaa"); // false 반환

		Validator lowerCaseValidator = new Validator(s -> s.matches("\\d+"));
		boolean b2 = lowerCaseValidator.validate("bbbb"); // true 반환
	}

	public interface ValidationStrategy {
		boolean execute(String s);
	}

	public static class IsAllLowerCase implements ValidationStrategy {

		@Override
		public boolean execute(String s) {
			return s.matches("[a-z]+");
		}
	}

	public static class IsNumeric implements ValidationStrategy {

		@Override
		public boolean execute(String s) {
			return s.matches("\\d+");
		}
	}

	public static class Validator {
		private final ValidationStrategy strategy;

		public Validator(ValidationStrategy strategy) {
			this.strategy = strategy;
		}

		public boolean validate(String s) {
			return strategy.execute(s);
		}
	}
}
