package com.jaenyeong.chapter_11;

import com.jaenyeong.chapter_11.DefaultVerion.Car;
import com.jaenyeong.chapter_11.DefaultVerion.Insurance;
import com.jaenyeong.chapter_11.DefaultVerion.Person;
import com.jaenyeong.chapter_11.OptionalVersion.OptionalCar;
import com.jaenyeong.chapter_11.OptionalVersion.OptionalInsurance;
import com.jaenyeong.chapter_11.OptionalVersion.OptionalPerson;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class OptionalMain {

	public static String getCarInsuranceName(Person person) {
		return person.getCar().getInsurance().getName();
	}

	// null 확인 코드 때문에 나머지 호출 체인의 들여쓰기 수준이 증가함
	public static String getCarInsuranceNameNullSafe1(Person person) {
		if (person != null) {
			Car car = person.getCar();
			if (car != null) {
				Insurance insurance = car.getInsurance();
				if (insurance != null) {
					return insurance.getName();
				}
			}
		}
		return "Unknown";
	}

	// null 확인 코드마다 출구가 생김
	public static String getCarInsuranceNameNullSafeV2(Person person) {
		if (person == null) {
			return "Unknown";
		}

		Car car = person.getCar();
		if (car == null) {
			return "Unknown";
		}

		Insurance insurance = car.getInsurance();
		if (insurance == null) {
			return "Unknown";
		}

		return insurance.getName();
	}

	public static void convertOptionalFromMap() {
		String name1 = null;
		Insurance insurance = new Insurance();
		if (insurance != null) {
			name1 = insurance.getName();
		}

		Optional<Insurance> optInsurance = Optional.ofNullable(insurance);
		Optional<String> name2 = optInsurance.map(Insurance::getName);
	}

	// 컴파일 되지 않음
	// (1)에서 Optional<Person>에 map(Person::getCar) 호출을 시도함 > flatMap()을 이용하면 문제가 해결됨.
	// (2)에서 Optional<Car>에 map(Car::getInsurance) 호출을 시도함 > flatMap()을 이용하면 문제가 해결됨.
	// (3) Insurance::getName은 평범한 문자열을 반환하므로 추가 "flatMap"은 필요가 없음.
//	public static String getCarInsuranceNameFromOptional(OptionalPerson person) {
//		Optional<OptionalPerson> optPerson = Optional.of(person);
//		Optional<String> name =
//				optPerson.map(OptionalPerson::getCar)     // (1)
//						.map(OptionalCar::getInsurance)   // (2)
//						.map(OptionalInsurance::getName); // (3)
//		return name.orElse("Unknown");
//	}

	public static String getCarInsuranceNameFromOptional(Optional<OptionalPerson> optPerson) {
		Optional<String> name =
				optPerson.flatMap(OptionalPerson::getCar)
						.flatMap(OptionalCar::getInsurance)
						.map(OptionalInsurance::getName);
		return name.orElse("Unknown");
	}

	public static Set<String> getCarInsuranceNames(List<OptionalPerson> persons) {
		return persons.stream()
				// 사람 목록을 각 사람이 보유한 자동차의 Optional<Car> 스트림으로 변환
				.map(OptionalPerson::getCar)
				// FlatMap 연산을 이용해 Optional<Car>을 해당 Optional<Insurance>로 변환
				.map(optCar -> optCar.flatMap(OptionalCar::getInsurance))
				// Optional<Insurance>를 해당 이름의 Optional<String>으로 매핑
				.map(optInsurance -> optInsurance.map(OptionalInsurance::getName))
				// Stream<Optional<String>>을 현재 이름을 포함하는 Stream<String>으로 변환
				.flatMap(Optional::stream)
				// 결과 문자열을 중복되지 않은 값을 갖도록 집합으로 수집
				.collect(toSet());
	}

	public Insurance findCheapestInsurance(OptionalPerson person, OptionalCar car) {
		// 다른 보험사에서 제공한 질의 서비스
		// 모든 데이터 비교
		Insurance cheapestCompany = new Insurance();
		return cheapestCompany;
	}

	public Optional<Insurance> nullSafeFindCheapestInsurance(Optional<OptionalPerson> person, Optional<OptionalCar> car) {
		if (person.isPresent() && car.isPresent()) {
			return Optional.of(findCheapestInsurance(person.get(), car.get()));
		} else {
			return Optional.empty();
		}
	}

	// Quiz 1
	public Optional<Insurance> refactorNullSafeFindCheapestInsurance(
			Optional<OptionalPerson> person, Optional<OptionalCar> car) {
		return person.flatMap(p -> car.map(c -> findCheapestInsurance(p, c)));
	}

	// Quiz 2
	public String quizGetCarInsuranceName(Optional<OptionalPerson> person, int minAge) {
		return person
				.filter(p -> p.getAge() >= minAge)
				.flatMap(OptionalPerson::getCar)
				.flatMap(OptionalCar::getInsurance)
				.map(OptionalInsurance::getName)
				.orElse("Unknown");
	}
}
