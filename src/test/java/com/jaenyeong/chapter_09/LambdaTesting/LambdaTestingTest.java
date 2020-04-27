package com.jaenyeong.chapter_09.LambdaTesting;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static com.jaenyeong.chapter_09.LambdaTesting.LambdaTesting.filter;
import static org.junit.Assert.assertEquals;

public class LambdaTestingTest {

	@Test
	public void testFilter() throws Exception {
		List<Integer> numbers = Arrays.asList(1, 2, 3, 4);
		List<Integer> even = filter(numbers, i -> i % 2 == 0);
		List<Integer> smallerThanThree = filter(numbers, i -> i < 3);
		assertEquals(Arrays.asList(2, 4), even);
		assertEquals(Arrays.asList(1, 2), smallerThanThree);
	}
}
