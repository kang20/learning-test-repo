package batch.springkafkapracticetest;

import static org.assertj.core.api.SoftAssertions.*;

import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import batch.springkafkapracticetest.config.consumer.RecordConsumer;
import batch.springkafkapracticetest.config.producer.KafkaProducerConfig;

@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext
@Import(value = {RecordConsumer.class, KafkaProducerConfig.class})
@EmbeddedKafka(
	partitions=10,
	brokerProperties = {"listeners=PLAINTEXT://localhost:9092", "port=9092"})
public class ProducerTest {
	@Autowired
	@Qualifier("kafkaTemplate")
	private KafkaTemplate<String, String> template1;
	@Autowired
	@Qualifier("kafkaTemplate2")
	private KafkaTemplate<String,String> template2;
	@Autowired
	private RecordConsumer recordConsumer;
	@Autowired
	private KafkaAdmin kafkaAdmin;

	@Value("${test.topic.name}")
	private String TOPIC_NAME;

	@Test
	void kafkaTemplate연습() throws ExecutionException, InterruptedException {
		// given
		String key = "key";
		String value = "value";

		// when
		SendResult<String, String> send = template1.send(TOPIC_NAME,key,value).get();
		template2.send(TOPIC_NAME,key,value).get();
		// then
		assertSoftly(softly -> {
			softly.assertThat(send).isNotNull();
			softly.assertThat(recordConsumer.getLatchCount()).isEqualTo(0);
			softly.assertThat(recordConsumer.getLatchCount2()).isEqualTo(0);
		});
	}

}
