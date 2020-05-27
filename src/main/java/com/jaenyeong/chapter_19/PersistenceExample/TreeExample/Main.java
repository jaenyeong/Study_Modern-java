package com.jaenyeong.chapter_19.PersistenceExample.TreeExample;

import static com.jaenyeong.chapter_19.PersistenceExample.TreeExample.Tree.TreeProcessor.*;

public class Main {

	public static void main(String[] args) {
		Tree t = new Tree("Mary", 22,
				new Tree("Emily", 20,
						new Tree("Alan", 50, null, null),
						new Tree("Georgie", 23, null, null)
				),
				new Tree("Tian", 29,
						new Tree("Raoul", 23, null, null),
						null
				)
		);

		// 발견 = 23
		System.out.printf("Raoul: %d%n", lookup("Raoul", -1, t));
		// 발견되지 않음 = -1
		System.out.printf("Jeff: %d%n", lookup("Jeff", -1, t));

		Tree f = fupdate("Jeff", 80, t);
		// 발견 = 80
		System.out.printf("Jeff: %d%n", lookup("Jeff", -1, f));

		Tree u = update("Jim", 40, t);
		// fupdate로 t가 바뀌지 않았으므로 Jeff는 발견되지 않음 = -1
		System.out.printf("Jeff: %d%n", lookup("Jeff", -1, u));
		// 발견 = 40
		System.out.printf("Jim: %d%n", lookup("Jim", -1, u));

		Tree f2 = fupdate("Jeff", 80, t);
		// 발견 = 80
		System.out.printf("Jeff: %d%n", lookup("Jeff", -1, f2));
		// t로 만든 f2는 위 update()에서 갱신되므로 Jim은 여전히 존재함 = 40
		System.out.printf("Jim: %d%n", lookup("Jim", -1, f2));
	}
}
