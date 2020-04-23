package com.jaenyeong.chapter_08;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Map.entry;

public class CreatingCollections {

	public static void main(String[] args) {
		creatingLists();
		creatingSets();
		creatingMaps();
	}

	private static void creatingLists() {
		// 기존
		System.out.println("--> Creating a list the old-fashioned way");
		List<String> friends = new ArrayList<>();
		friends.add("Raphael");
		friends.add("Olivia");
		friends.add("Thibaut");
		System.out.println(friends);

		// List를 asList를 사용하여 생성
		System.out.println("--> Using Arrays.asList()");
		List<String> friends2 = Arrays.asList("Raphael", "Olivia");
		// 객체 변경은 가능
		friends2.set(0, "Richard");
		System.out.println(friends2);

		try {
			friends2.add("Thibaut");
			System.out.println("We shouldn't get here...");
		} catch (UnsupportedOperationException e) {
			System.out.println("As expected, we can't add items to a list created with Arrays.asList().");
		}

		// Set을 asList를 사용하여 생성
		System.out.println("--> Creating a Set from a List");
		Set<String> friends3 = new HashSet<>(Arrays.asList("Raphael", "Olivia", "Thibaut"));
		System.out.println(friends3);

		// Set을 Stream을 사용하여 생성
		System.out.println("--> Creating a Set from a Stream");
		Set<String> friends4 = Stream.of("Raphael", "Olivia", "Thibaut")
				.collect(Collectors.toSet());
		System.out.println(friends4);

		// List를 List.of를 사용하여 생성
		System.out.println("--> Creating a List with List.of()");
		List<String> friends5 = List.of("Raphael", "Olivia", "Thibaut");
		System.out.println(friends5);

		try {
			friends5.add("Chih-Chun");
			System.out.println("We shouldn't get here...");
		} catch (UnsupportedOperationException e) {
			System.out.println("As expected, we can't add items to a list created with List.of().");
		}

		try {
			friends5.set(1, "Chih-Chun");
			System.out.println("We shouldn't get here...");
		} catch (UnsupportedOperationException e) {
			System.out.println("Neither can we replace items in such a list.");
		}
	}

	private static void creatingSets() {
		// Set을 Set.of을 사용하여 생성
		System.out.println("--> Creating a Set with Set.of()");
		Set<String> friends = Set.of("Raphael", "Olivia", "Thibaut");
		System.out.println(friends);

		System.out.println("--> Trying to pass duplicate items to Set.of()");
		try {
			// 중복 요소 삽입시 예외 확인
			Set<String> friends2 = Set.of("Raphael", "Olivia", "Olivia");
			System.out.println("We shouldn't get here...");
		} catch (IllegalArgumentException e) {
			System.out.println("As expected, duplicate items are not allowed with Set.of().");
		}
	}

	private static void creatingMaps() {
		// Map을 Map.of을 사용하여 생성
		System.out.println("--> Creating a Map with Map.of()");
		Map<String, Integer> ageOfFriends = Map.of("Raphael", 30, "Olivia", 25, "Thibaut", 26);
		System.out.println(ageOfFriends);

		// Map.ofEntries를 사용하여 생성시 인수로 필요한 Entry 객체를 생성하는 entry 팩토리 메서드 사용
		System.out.println("--> Creating a Map with Map.ofEntries()");
		Map<String, Integer> ageOfFriends2 = Map.ofEntries(
				entry("Raphael", 30),
				entry("Olivia", 25),
				entry("Thibaut", 26));
		System.out.println(ageOfFriends2);
	}
}
