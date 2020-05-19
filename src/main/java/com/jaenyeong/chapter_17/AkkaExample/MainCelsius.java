package com.jaenyeong.chapter_17.AkkaExample;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.AsPublisher;
import akka.stream.javadsl.JavaFlowSupport.Sink;
import akka.stream.javadsl.JavaFlowSupport.Source;

import java.util.concurrent.Flow.Publisher;

public class MainCelsius {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("temp-info");
		Materializer materializer = ActorMaterializer.create(system);

		Publisher<TempInfo> publisher =
				Source.fromPublisher(getCelsiusTemperatures("New York"))
						.runWith(Sink.asPublisher(AsPublisher.WITH_FANOUT), materializer);
		publisher.subscribe(new TempSubscriber());

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	public static Publisher<TempInfo> getCelsiusTemperatures(String town) {
		return subscriber -> {
			TempProcessor processor = new TempProcessor();
			processor.subscribe(subscriber);
			processor.onSubscribe(new TempSubscription(processor, town));
		};
	}
}
