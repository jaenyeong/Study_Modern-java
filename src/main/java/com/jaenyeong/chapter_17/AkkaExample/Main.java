package com.jaenyeong.chapter_17.AkkaExample;

import akka.actor.ActorSystem;
import akka.stream.ActorMaterializer;
import akka.stream.Materializer;
import akka.stream.javadsl.AsPublisher;
import akka.stream.javadsl.JavaFlowSupport.Sink;
import akka.stream.javadsl.JavaFlowSupport.Source;

import java.util.concurrent.Flow.Publisher;

public class Main {

	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create("temp-info");
		// ActorMaterializer.create() is deprecated
		Materializer materializer = ActorMaterializer.create(system);

		Publisher<TempInfo> publisher =
				Source.fromPublisher(getTemperatures("New York"))
						.runWith(Sink.asPublisher(AsPublisher.WITH_FANOUT), materializer);
		publisher.subscribe(new TempSubscriber());

		try {
			Thread.sleep(10000L);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
	}

	private static Publisher<TempInfo> getTemperatures(String town) {
		return subscriber -> subscriber.onSubscribe(new TempSubscription(subscriber, town));
	}
}
