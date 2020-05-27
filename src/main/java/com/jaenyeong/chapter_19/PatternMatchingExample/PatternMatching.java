package com.jaenyeong.chapter_19.PatternMatchingExample;

import java.util.function.Function;
import java.util.function.Supplier;

public class PatternMatching {

	public static void main(String[] args) {
		Expr e = new BinOp("+", new Number(5), new Number(0));
		Expr match = simplify(e);
		System.out.println(match); // 5 출력

//		simplify();

		Expr e2 = new BinOp("+", new Number(5), new BinOp("*", new Number(3), new Number(4)));
		Integer result = evaluate(e2);
		System.out.println(e2 + " = " + result);
	}

	public static Expr simplify(Expr e) {
		TriFunction<String, Expr, Expr, Expr> binOpCase = (opName, left, right) -> {  // BinOp 표현식 처리
			// 덧셈 처리
			if ("+".equals(opName)) {
				if (left instanceof Number && ((Number) left).val == 0) {
					return right;
				}
				if (right instanceof Number && ((Number) right).val == 0) {
					return left;
				}
			}
			// 곱셈 처리
			if ("*".equals(opName)) {
				if (left instanceof Number && ((Number) left).val == 1) {
					return right;
				}
				if (right instanceof Number && ((Number) right).val == 1) {
					return left;
				}
			}
			return new BinOp(opName, left, right);
		};

		// 숫자 처리
//		Function<Integer, Expr> numCase = val -> new Number(val); 람다를 메서드 참조로 변경
		Function<Integer, Expr> numCase = Number::new;

		// 수식을 인식할 수 없을 때 기본처리
		Supplier<Expr> defaultCase = () -> new Number(0);

		return patternMatchExpr(e, binOpCase, numCase, defaultCase);
	}

	// 책에 없는 예제 소스
	// 반환 타입, 하단 부분 로직 다름
//	static void simplify() {
//		TriFunction<String, Expr, Expr, Expr> binopcase = (opname, left, right) -> {
//			if ("+".equals(opname)) {
//				if (left instanceof Number && ((Number) left).val == 0) {
//					return right;
//				}
//				if (right instanceof Number && ((Number) right).val == 0) {
//					return left;
//				}
//			}
//			if ("*".equals(opname)) {
//				if (left instanceof Number && ((Number) left).val == 1) {
//					return right;
//				}
//				if (right instanceof Number && ((Number) right).val == 1) {
//					return left;
//				}
//			}
//			return new BinOp(opname, left, right);
//		};
//
//		Function<Integer, Expr> numcase = val -> new Number(val);
//		Supplier<Expr> defaultcase = () -> new Number(0);
//
//		Expr e = new BinOp("+", new Number(5), new Number(0));
//		Expr match = patternMatchExpr(e, binopcase, numcase, defaultcase);
//		if (match instanceof Number) {
//			System.out.println("Number: " + match);
//		} else if (match instanceof BinOp) {
//			System.out.println("BinOp: " + match);
//		}
//	}

	// 책에 없는 예제 소스
	static Integer evaluate(Expr e) {
		Function<Integer, Integer> numCase = val -> val;
		Supplier<Integer> defaultCase = () -> 0;
		TriFunction<String, Expr, Expr, Integer> binOpCase = (opname, left, right) -> {
			if ("+".equals(opname)) {
				if (left instanceof Number && right instanceof Number) {
					return ((Number) left).val + ((Number) right).val;
				}
				if (right instanceof Number && left instanceof BinOp) {
					return ((Number) right).val + evaluate(left);
				}
				if (left instanceof Number && right instanceof BinOp) {
					return ((Number) left).val + evaluate(right);
				}
				if (left instanceof BinOp && right instanceof BinOp) {
					return evaluate(left) + evaluate(right);
				}
			}
			if ("*".equals(opname)) {
				if (left instanceof Number && right instanceof Number) {
					return ((Number) left).val * ((Number) right).val;
				}
				if (right instanceof Number && left instanceof BinOp) {
					return ((Number) right).val * evaluate(left);
				}
				if (left instanceof Number && right instanceof BinOp) {
					return ((Number) left).val * evaluate(right);
				}
				if (left instanceof BinOp && right instanceof BinOp) {
					return evaluate(left) * evaluate(right);
				}
			}
			return defaultCase.get();
		};

		return patternMatchExpr(e, binOpCase, numCase, defaultCase);
	}

	static class Expr {
	}

	static class Number extends Expr {
		int val;

		public Number(int val) {
			this.val = val;
		}

		@Override
		public String toString() {
			return "" + val;
		}
	}

	static class BinOp extends Expr {
		String opName;
		Expr left, right;

		public BinOp(String opName, Expr left, Expr right) {
			this.opName = opName;
			this.left = left;
			this.right = right;
		}

//		public Expr accept(SimplifyExprVisitor v) {
//			return v.visit(this);
//		}

		@Override
		public String toString() {
			return "(" + left + " " + opName + " " + right + ")";
		}
	}

//	static class SimplifyExprVisitor {
//
//		public Expr visit(BinOp e) {
//			if ("+".equals(e.opName) && e.right instanceof Number && ...) {
//				return e.left;
//			}
//			return e;
//		}
//	}

	static <T> T myIf(boolean b, Supplier<T> trueCase, Supplier<T> falseCase) {
		return b ? trueCase.get() : falseCase.get();
	}

	interface TriFunction<S, T, U, R> {
		R apply(S s, T t, U u);
	}

	static <T> T patternMatchExpr(
			Expr e,
			TriFunction<String, Expr, Expr, T> binOpCase,
			Function<Integer, T> numCase,
			Supplier<T> defaultCase) {

		return (e instanceof BinOp) ?
				binOpCase.apply(((BinOp) e).opName, ((BinOp) e).left, ((BinOp) e).right) :
				(e instanceof Number) ?
						numCase.apply(((Number) e).val) :
						defaultCase.get();
	}

	// 위 메서드의 if-then-else 형태
//	static <T> T patternMatchExpr(
//			Expr e,
//			TriFunction<String, Expr, Expr, T> binopcase,
//			Function<Integer, T> numcase,
//			Supplier<T> defaultcase) {
//
//		if (e instanceof BinOp) {
//			return binopcase.apply(((BinOp) e).opname, ((BinOp) e).left, ((BinOp) e).right);
//		} else if (e instanceof Number) {
//			return numcase.apply(((Number) e).val);
//		} else {
//			return defaultcase.get();
//		}
//	}
}
