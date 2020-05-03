package com.jaenyeong.chapter_12;

import java.time.*;
import java.time.chrono.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Locale;
import java.util.TimeZone;

public class Example {

	public static void main(String[] args) {
		exampleLocalDate();
		exampleChronoField();
		exampleLocalTime();
		exampleLocalDateTime();
		exampleInstant();
		exampleDuration();
		examplePeriod();
		exampleParsingAndFormatting();
		exampleLocalDatePlusAndMinus();
		exampleDateTimeFormatter();
		examplePatternDateTimeFormatter();
		exampleLocaleDateTimeFormatter();
		exampleDateTimeFormatterBuilder();
		exampleZoneId();
		exampleZoneOffset();
		exampleHijrahDate();
	}

	public static void exampleLocalDate() {
		System.out.println("[exampleLocalDate]");

		// 정적 팩토리 메서드 of를 사용
		// 2017-09-21
		LocalDate date = LocalDate.of(2017, 9, 21);
		int year = date.getYear();           // 2017
		Month month = date.getMonth();       // SEPTEMBER
		int day = date.getDayOfMonth();      // 21
		DayOfWeek dow = date.getDayOfWeek(); // THURSDAY
		int length = date.lengthOfMonth();   // 31 (3월의 일 수)
		boolean leap = date.isLeapYear();    // false (윤년이 아님)

		// 정적 팩토리 메서드 now
		// 시스템 시계의 정보를 이용해 현재 날짜 정보를 얻음
		LocalDate today = LocalDate.now();

		// 날짜와 시간 문자열로 인스턴스 생성
		LocalDate ldt = LocalDate.parse("2017-09-21");

		System.out.println("date: " + date);

		System.out.println("year: " + year);
		System.out.println("month: " + month);
		System.out.println("day: " + day);
		System.out.println("dow: " + dow);
		System.out.println("length: " + length);
		System.out.println("leap: " + leap);

		System.out.println("today: " + today);
		System.out.println("ldt: " + ldt);
	}

	public static void exampleChronoField() {
		System.out.println("[exampleChronoField]");

		LocalDate date = LocalDate.of(2017, 9, 21);

		// ChronoField는 TemporalField 인터페이스를 정의
		int year = date.get(ChronoField.YEAR);
		int month = date.get(ChronoField.MONTH_OF_YEAR);
		int day = date.get(ChronoField.DAY_OF_MONTH);

		// 내장 메서드 getYear(), getMonthValue(), getDayOfMonth() 등을 이용하여 가독성 높임
		int getYear = date.getYear();
		int getMonth = date.getMonthValue();
		int getDay = date.getDayOfMonth();

		System.out.println("date: " + date);

		System.out.println("year: " + year);
		System.out.println("month: " + month);
		System.out.println("day: " + day);

		System.out.println("getYear: " + getYear);
		System.out.println("getMonth: " + getMonth);
		System.out.println("getDay: " + getDay);
	}

	public static void exampleLocalTime() {
		System.out.println("[exampleLocalTime]");

		// 정적 팩토리 메서드 of를 사용
		// 13:45:20
		LocalTime time = LocalTime.of(13, 45, 20);
		int hour = time.getHour();     // 13
		int minute = time.getMinute(); // 45
		int second = time.getSecond(); // 20

		// 날짜와 시간 문자열로 인스턴스 생성
		LocalTime ldt = LocalTime.parse("13:45:20");

		System.out.println("time: " + time);

		System.out.println("hour: " + hour);
		System.out.println("minute: " + minute);
		System.out.println("second: " + second);

		System.out.println("ldt: " + ldt);
	}

	public static void exampleLocalDateTime() {
		System.out.println("[exampleLocalDateTime]");

		LocalDate date = LocalDate.of(2017, 9, 21);
		LocalTime time = LocalTime.of(13, 45, 20);

		// 2017-09-21T13:45:20
		LocalDateTime ldt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
		LocalDateTime ldt2 = LocalDateTime.of(date, time);
		// LocalDate의 atTime 메서드에 시간을 제공
		LocalDateTime ldt3 = date.atTime(13, 45, 20);
		LocalDateTime ldt4 = date.atTime(time);
		// LocalTime의 atDate 메서드에 날짜를 제공
		LocalDateTime ldt5 = time.atDate(date);

		// toLocalDate, toLocalTime 메서드로 LocalDate, LocalTime 인스턴스 추출
		LocalDate date1 = ldt1.toLocalDate();
		LocalTime time1 = ldt1.toLocalTime();

		System.out.println("date: " + date);
		System.out.println("time: " + time);

		System.out.println("ldt1: " + ldt1);
		System.out.println("ldt2: " + ldt2);
		System.out.println("ldt3: " + ldt3);
		System.out.println("ldt4: " + ldt4);
		System.out.println("ldt5: " + ldt5);

		System.out.println("date1: " + date1);
		System.out.println("time1: " + time1);
	}

	public static void exampleInstant() {
		System.out.println("[exampleInstant]");

		Instant instant1 = Instant.ofEpochSecond(3);
		Instant instant2 = Instant.ofEpochSecond(3, 0);
		// 2초 이후의 1억 나노초(1초)
		Instant instant3 = Instant.ofEpochSecond(2, 1_000_000_000);
		// 4초 이전의 1억 나노초(1초)
		Instant instant4 = Instant.ofEpochSecond(4, -1_000_000_000);

		System.out.println("instant1: " + instant1);
		System.out.println("instant2: " + instant2);
		System.out.println("instant3: " + instant3);
		System.out.println("instant4: " + instant4);
	}

	public static void exampleDuration() {
		System.out.println("[exampleDuration]");

		LocalTime time1 = LocalTime.of(13, 45, 20);
		LocalTime time2 = LocalTime.of(13, 55, 20);

		LocalDateTime ldt1 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 45, 20);
		LocalDateTime ldt2 = LocalDateTime.of(2017, Month.SEPTEMBER, 21, 13, 55, 20);

		Instant instant1 = Instant.ofEpochSecond(3);
		Instant instant2 = Instant.ofEpochSecond(4);

		Duration d1 = Duration.between(time1, time2);
		Duration d2 = Duration.between(ldt1, ldt2);
		Duration d3 = Duration.between(instant1, instant2);

		System.out.println("time1: " + time1);
		System.out.println("time2: " + time2);

		System.out.println("ldt1: " + ldt1);
		System.out.println("ldt2: " + ldt2);

		System.out.println("instant1: " + instant1);
		System.out.println("instant2: " + instant2);

		System.out.println("d1: " + d1);
		System.out.println("d2: " + d2);
		System.out.println("d3: " + d3);
	}

	public static void examplePeriod() {
		System.out.println("[examplePeriod]");

		Period tenDays = Period.between(
				LocalDate.of(2017, 9, 11),
				LocalDate.of(2017, 9, 21));

		System.out.println(tenDays);
	}

	public static void exampleCreateDurationAndPeriod() {
		System.out.println("[exampleCreateDurationAndPeriod]");

		Duration threeMinutes1 = Duration.ofMinutes(3);
		Duration threeMinutes2 = Duration.of(3, ChronoUnit.MINUTES);

		Period tenDays = Period.ofDays(10);
		Period threeWeeks = Period.ofWeeks(3);
		Period twoYearsSixMonthOneDay = Period.of(2, 6, 1);

		System.out.println("threeMinutes1: " + threeMinutes1);
		System.out.println("threeMinutes2: " + threeMinutes2);

		System.out.println("tenDays: " + tenDays);
		System.out.println("threeWeeks: " + threeWeeks);
		System.out.println("twoYearsSixMonthOneDay: " + twoYearsSixMonthOneDay);
	}

	public static void exampleParsingAndFormatting() {
		System.out.println("[exampleParsingAndFormatting]");

		LocalDate date1 = LocalDate.of(2017, 9, 21);
		LocalDate date2 = date1.withYear(2011);
		LocalDate date3 = date2.withDayOfMonth(25);
		LocalDate date4 = date3.with(ChronoField.MONTH_OF_YEAR, 2);

		System.out.println("date1: " + date1);
		System.out.println("date2: " + date2);
		System.out.println("date3: " + date3);
		System.out.println("date4: " + date4);
	}

	public static void exampleLocalDatePlusAndMinus() {
		System.out.println("[exampleLocalDatePlusAndMinus]");

		LocalDate date1 = LocalDate.of(2017, 9, 21);
		LocalDate date2 = date1.plusWeeks(1);
		LocalDate date3 = date2.minusYears(6);
		LocalDate date4 = date3.plus(6, ChronoUnit.MONTHS);

		System.out.println("date1: " + date1);
		System.out.println("date2: " + date2);
		System.out.println("date3: " + date3);
		System.out.println("date4: " + date4);
	}

	public static void exampleDateTimeFormatter() {
		System.out.println("[exampleDateTimeFormatter]");

		LocalDate date = LocalDate.of(2014, 3, 18);
		String s1 = date.format(DateTimeFormatter.BASIC_ISO_DATE); // 20140318
		String s2 = date.format(DateTimeFormatter.ISO_LOCAL_DATE); // 2014-03-18

		System.out.println("s1: " + s1);
		System.out.println("s2: " + s2);

		LocalDate date1 = LocalDate.parse("20140318", DateTimeFormatter.BASIC_ISO_DATE);
		LocalDate date2 = LocalDate.parse("2014-03-18", DateTimeFormatter.ISO_LOCAL_DATE);

		System.out.println("date1: " + date1);
		System.out.println("date2: " + date2);
	}

	public static void examplePatternDateTimeFormatter() {
		System.out.println("[examplePatternDateTimeFormatter]");

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		LocalDate date1 = LocalDate.of(2014, 3, 18);
		String formattedDate = date1.format(formatter);
		LocalDate date2 = LocalDate.parse(formattedDate, formatter);

		System.out.println("formatter: " + formatter);
		System.out.println("date1: " + date1);
		System.out.println("formattedDate: " + formattedDate);
		System.out.println("date2: " + date2);
	}

	public static void exampleLocaleDateTimeFormatter() {
		System.out.println("[exampleLocaleDateTimeFormatter]");

		DateTimeFormatter italianFormatter = DateTimeFormatter.ofPattern("d. MMMM yyyy", Locale.ITALIAN);
		LocalDate date1 = LocalDate.of(2014, 3, 18);
		String formattedDate = date1.format(italianFormatter); // 18. marzo 2014
		LocalDate date2 = LocalDate.parse(formattedDate, italianFormatter);

		System.out.println("italianFormatter: " + italianFormatter);
		System.out.println("date1: " + date1);
		System.out.println("formattedDate: " + formattedDate);
		System.out.println("date2: " + date2);
	}

	public static void exampleDateTimeFormatterBuilder() {
		System.out.println("[exampleDateTimeFormatterBuilder]");

		DateTimeFormatter italianFormatter = new DateTimeFormatterBuilder()
				.appendText(ChronoField.DAY_OF_MONTH)
				.appendLiteral(". ")
				.appendText(ChronoField.MONTH_OF_YEAR)
				.appendLiteral(" ")
				.appendText(ChronoField.YEAR)
				.parseCaseInsensitive()
				.toFormatter(Locale.ITALIAN);

		System.out.println("italianFormatter: " + italianFormatter);
	}

	public static void exampleZoneId() {
		System.out.println("[exampleZoneId]");

		ZoneId romeZone = ZoneId.of("Europe/Rome");
		System.out.println("romeZone: " + romeZone);

		ZoneId zoneId = TimeZone.getDefault().toZoneId();
		System.out.println("zoneId: " + zoneId);

		LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
		ZonedDateTime zdt1 = date.atStartOfDay(romeZone);
		LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
		ZonedDateTime zdt2 = dateTime.atZone(romeZone);
		Instant instant = Instant.now();
		ZonedDateTime zdt3 = instant.atZone(romeZone);

		// ZoneId를 이용해 LocalDateTime을 Instant로 변경
		LocalDateTime timeFromInstant = LocalDateTime.ofInstant(instant, romeZone);

		System.out.println("date: " + date);
		System.out.println("zdt1: " + zdt1);
		System.out.println("dateTime: " + dateTime);
		System.out.println("zdt2: " + zdt2);
		System.out.println("instant: " + instant);
		System.out.println("zdt3: " + zdt3);
		System.out.println("timeFromInstant: " + timeFromInstant);
	}

	public static void exampleZoneOffset() {
		System.out.println("[exampleZoneOffset]");

		ZoneOffset newYorkOffset = ZoneOffset.of("-05:00");
		System.out.println("newYorkOffset: " + newYorkOffset);

		LocalDateTime dateTime = LocalDateTime.of(2014, Month.MARCH, 18, 13, 45);
		OffsetDateTime dateTimeInNewYork = OffsetDateTime.of(dateTime, newYorkOffset);

		System.out.println("dateTime: " + dateTime);
		System.out.println("dateTimeInNewYork: " + dateTimeInNewYork);
	}

	public static void exampleChronoLocalDate() {
		System.out.println("[exampleChronoLocalDate]");

		LocalDate date = LocalDate.of(2014, Month.MARCH, 18);
		JapaneseDate japaneseDate = JapaneseDate.from(date);

		System.out.println("date: " + date);
		System.out.println("japaneseDate: " + japaneseDate);

		Chronology japaneseChronology = Chronology.ofLocale(Locale.JAPAN);
		ChronoLocalDate now = japaneseChronology.dateNow();

		System.out.println("japaneseChronology: " + japaneseChronology);
		System.out.println("now: " + now);
	}

	public static void exampleHijrahDate() {
		System.out.println("[exampleHijrahDate]");

		HijrahDate ramadanDate = HijrahDate.now().with(ChronoField.DAY_OF_MONTH, 1)
				// 현재 Hijrah 날짜를 얻음
				// 얻은 날짜를 Ramadan의 첫 번째 날, 즉 9번째 달로 바꿈
				.with(ChronoField.MONTH_OF_YEAR, 9);

		System.out.println("Ramadan starts on "
				// INSTANCE는 IsoChronology 클래스의 정적 인스턴스
				+ IsoChronology.INSTANCE.date(ramadanDate)
				+ " and ends on "
				// Ramadan 1438은 2017-05-26에 시작해서 2017-06-24에 종료됨
				+ IsoChronology.INSTANCE.date(ramadanDate.with(TemporalAdjusters.lastDayOfMonth())));

//		System.out.println("ramadanDate: " + ramadanDate);
	}
}
