package com.jaenyeong.chapter_10.ErrorExample;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ErrorExample {
	static final String FILE_NAME = "";

	public static void java7FileErrorExample() throws IOException {
		List<String> errors = new ArrayList<>();

		int errorCount = 0;

		BufferedReader bufferedReader = new BufferedReader(new FileReader(FILE_NAME));

		String line = bufferedReader.readLine();
		while (errorCount < 40 && line != null) {
			if (line.startsWith("ERROR")) {
				errors.add(line);
				errorCount++;
			}
			line = bufferedReader.readLine();
		}
	}

	public static void lambdaFileErrorExample() throws IOException {
		List<String> errors = Files.lines(Paths.get(FILE_NAME)) // 파일을 열어서 문자열 스트림을 만듦
				.filter(line -> line.startsWith("ERROR")) // "ERROR"로 시작하는 행을 필터링
				.limit(40) // 결과를 첫 40행으로 제한
				.collect(toList()); // 결과 문자열을 리스트로 수집
	}
}
