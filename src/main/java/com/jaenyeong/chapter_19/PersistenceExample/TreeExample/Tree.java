package com.jaenyeong.chapter_19.PersistenceExample.TreeExample;

public class Tree {
	private String key;
	private int val;
	private Tree left, right;

	public Tree(String k, int v, Tree l, Tree r) {
		this.key = k;
		this.val = v;
		this.left = l;
		this.right = r;
	}

	static class TreeProcessor {

		public static int lookup(String k, int defaultVal, Tree t) {
			if (t == null) {
				return defaultVal;
			}

			if (k.equals(t.key)) {
				return t.val;
			}

			return lookup(k, defaultVal, k.compareTo(t.key) < 0 ? t.left : t.right);
		}

		// 첫 번째 update 메서드
//		public static void update(String k, int newVal, Tree t) {
//			if (t == null) {
//				// 새로운 노드를 추가해야함
//			} else if (k.equals(t.key)) {
//				t.val = newVal;
//			} else {
//				update(k, newVal, k.compareTo(t.key) < 0 ? t.left : t.right);
//			}
//		}

		// 두 번째 update 메서드
		public static Tree update(String k, int newVal, Tree t) {
			if (t == null) {
				t = new Tree(k, newVal, null, null);
			} else if (k.equals(t.key)) {
				t.val = newVal;
			} else if (k.compareTo(t.key) < 0) {
				t.left = update(k, newVal, t.left);
			} else {
				t.right = update(k, newVal, t.right);
			}
			return t;
		}

		public static Tree fupdate(String k, int newVal, Tree t) {
			return (t == null) ?
					new Tree(k, newVal, null, null) :
					k.equals(t.key) ?
							new Tree(k, newVal, t.left, t.right) :
							k.compareTo(t.key) < 0 ?
									new Tree(t.key, t.val, fupdate(k, newVal, t.left), t.right) :
									new Tree(t.key, t.val, t.left, fupdate(k, newVal, t.right));

		}
	}
}


