package com.jaenyeong.chapter_10.SpringIntegration;

//@configuration
//@EnableIntegration
public class MyConfiguration {
//	@Bean
//	public MessageSource<?> integerMessageSource() {
//		// 호출시 AtomicInteger를 증가 시키는 새 MessageSource를 생성
//		MethodInvokingMessageSource source = new MethodInvokingMessageSource();
//		source.setObject(new AtomicInteger());
//		source.setMethodName("getAndIncrement");
//		return source;
//	}
//
//	@Bean
//	public DirectChannel inputChannel() {
//		// MessageSource에서 도착하는 데이터를 나르는 채널
//		return new DirectChannel();
//	}
//
//	@Bean
//	public IntegrationFlow myFlow() {
//		return IntegrationFlows
//				// 기존에 정의한 MessageSource를 IntegrationFlow의 입력으로 사용
//				.from(this.integerMessageSource(),
//						// MessageSource를 폴링하면서 MessageSource가 나르는 데이터를 가져옴
//						c -> c.poller(Pollers.fixedRate(10)))
//				.channel(this.inputChannel())
//				// 짝수만 거름
//				.filter((Integer p) -> p % 2 == 0)
//				// MessageSource에서 가져온 정수를 문자열로 변환
//				.transform(Object::toString)
//				// queueChannel을 IntegrationFlow의 결과로 설정
//				.channel(MessageChannels.queue("queueChannel"))
//				// IntegrationFlow 만들기를 끝나고 반환
//				.get();
//	}
//
//	@Bean
//	public IntegrationFlow myFlow() {
//		return flow -> flow
//				.filter((Integer p) -> p % 2 == 0)
//				.transform(Object::toString)
//				.handle(System.out::println);
//	}
}
