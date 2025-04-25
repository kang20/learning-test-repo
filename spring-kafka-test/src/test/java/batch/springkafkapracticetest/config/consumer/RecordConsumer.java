package batch.springkafkapracticetest.config.consumer;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class RecordConsumer {
	private final CountDownLatch latch = new CountDownLatch(2);
	private final CountDownLatch latch2 = new CountDownLatch(2);
	private String payload;

	@KafkaListener(
		topics = "${test.topic.name}"
		,groupId = "test-consumer-group")
	public void receive(String message) {
		this.payload = message;
		latch.countDown();
	}

	@KafkaListener(
		topics = "${test.topic.name}",
		groupId = "test-consumer-group2"
	)
	public void receive2(String message) {
		this.payload = message;
		latch2.countDown();
	}

	public boolean awaitMessage(String expected, long timeout, TimeUnit unit) throws InterruptedException {
		return latch.await(timeout, unit) && expected.equals(payload);
	}

	public int getLatchCount() {
		return (int)latch.getCount();
	}

	public int getLatchCount2() {
		return (int)latch2.getCount();
	}
}
