package com.jaenyeong.chapter_12;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.*;

public class Quiz {

	public static void main(String[] args) {
		localDate();
	}

	public static void localDate() {
		LocalDate date = LocalDate.of(2014, 3, 18);
		System.out.println(date);

		date = date.with(ChronoField.MONTH_OF_YEAR, 9);
		System.out.println(date);

		date = date.plusYears(2).minusDays(10);
		System.out.println(date);

		date.withYear(2011);
		System.out.println(date);
	}

	public static void lambdaTemporalAdjuster() {
		LocalDate date = LocalDate.now();
		date = date.with(temporal -> {
			// 현재 날짜 읽기
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));

			// 일반적으로 하루 추가
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY) { // 오늘이 금요일이면 3일 추가
				dayToAdd = 3;
			} else if (dow == DayOfWeek.SATURDAY) { // 오늘이 토요일이면 2일 추가
				dayToAdd = 2;
			}

			// 적정한 날 수만큼 추가된 날짜를 반환
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		});
	}

	public static void exampleTemporalAdJusters() {
		LocalDate date = LocalDate.now();

		TemporalAdjuster nextWorkingDay = TemporalAdjusters.ofDateAdjuster(
				temporal -> {
					// 현재 날짜 읽기
					DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));

					// 일반적으로 하루 추가
					int dayToAdd = 1;
					if (dow == DayOfWeek.FRIDAY) { // 오늘이 금요일이면 3일 추가
						dayToAdd = 3;
					} else if (dow == DayOfWeek.SATURDAY) { // 오늘이 토요일이면 2일 추가
						dayToAdd = 2;
					}

					// 적정한 날 수만큼 추가된 날짜를 반환
					return temporal.plus(dayToAdd, ChronoUnit.DAYS);
				}
		);

		date.with(nextWorkingDay);
	}

	public static class NextWorkingDay implements TemporalAdjuster {

		@Override
		public Temporal adjustInto(Temporal temporal) {
			// 현재 날짜 읽기
			DayOfWeek dow = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));

			// 일반적으로 하루 추가
			int dayToAdd = 1;
			if (dow == DayOfWeek.FRIDAY) { // 오늘이 금요일이면 3일 추가
				dayToAdd = 3;
			} else if (dow == DayOfWeek.SATURDAY) { // 오늘이 토요일이면 2일 추가
				dayToAdd = 2;
			}

			// 적정한 날 수만큼 추가된 날짜를 반환
			return temporal.plus(dayToAdd, ChronoUnit.DAYS);
		}
	}
}
