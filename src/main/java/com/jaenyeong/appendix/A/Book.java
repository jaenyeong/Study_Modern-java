package com.jaenyeong.appendix.A;

import java.util.Arrays;

@Author(name = "Raoul")
@Author(name = "Mario")
@Author(name = "Alan")
public class Book {

	public static void main(String[] args) {
		Author[] authors = Book.class.getAnnotationsByType(Author.class);
//		Arrays.asList(authors).stream().forEach(a -> {
//			System.out.println(a.name());
//		});
		Arrays.stream(authors)
				.forEach(a -> {
					System.out.println(a.name());
				});
	}
}
