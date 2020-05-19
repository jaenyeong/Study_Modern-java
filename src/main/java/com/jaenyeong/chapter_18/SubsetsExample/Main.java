package com.jaenyeong.chapter_18.SubsetsExample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<List<Integer>> subs = subsets(Arrays.asList(1, 4, 9));
		subs.forEach(System.out::println);
	}

	public static List<List<Integer>> subsets(List<Integer> list) {
		// 입력 리스트가 비어있다면 빈 리스트 자신이 서브집합
		if (list.isEmpty()) {
			List<List<Integer>> ans = new ArrayList<>();
			ans.add(Collections.emptyList());
			return ans;
		}
		Integer first = list.get(0);
		List<Integer> rest = list.subList(1, list.size());

		// 빈 리스트가 아니면 먼저 하나의 요소를 꺼내고
		// 나머지 요소의 모든 서브집합을 찾아서 subans로 전달
		// subans는 절반의 정답을 포함
		List<List<Integer>> subans = subsets(rest);
		// 정답의 나머지 절반을 포함하는 subans2는 subans의 모든 리스트에 처음 꺼낸 요소를 앞에 추가해서 만듦
		List<List<Integer>> subans2 = insertAll(first, subans);

		// subans, subans2를 연결하면 정답 완성
		return concat(subans, subans2);
	}

	private static <T> List<List<T>> insertAll(T first, List<List<T>> lists) {
		List<List<T>> result = new ArrayList<>();

		for (List<T> l : lists) {
			// 리스트를 복사한 다음에 복사한 리스트에 요소를 추가
			// 구조체가 가변이더라도 저수준 구조를 복사하진 않음
			// Integer는 가변이 아님
			List<T> copyList = new ArrayList<>();
			copyList.add(first);
			copyList.addAll(l);
			result.add(copyList);
		}

		return result;
	}

	// 실무에서 사용하지 말 것
	private static <T> List<List<T>> concat(List<List<T>> a, List<List<T>> b) {
		List<List<T>> r = new ArrayList<>(a);
		r.addAll(b);
		return r;
	}
}
