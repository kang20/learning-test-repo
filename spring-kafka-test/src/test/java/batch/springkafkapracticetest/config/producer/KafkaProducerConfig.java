package batch.springkafkapracticetest.config.producer;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
public class KafkaProducerConfig {

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		Map<String, Object> producerProps = new HashMap<>();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProps.put(ProducerConfig.ACKS_CONFIG, "all");
		producerProps.put(ProducerConfig.RETRIES_CONFIG,1000);
		producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
		ProducerFactory<String,String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
		return new KafkaTemplate<>(producerFactory);
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate2(KafkaProperties kafkaProperties) {
		Map<String, Object> producerProps = new HashMap<>();
		producerProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
		producerProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		producerProps.put(ProducerConfig.ACKS_CONFIG, "all");
		producerProps.put(ProducerConfig.RETRIES_CONFIG,1000);
		producerProps.put(ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG, false);
		ProducerFactory<String,String> producerFactory = new DefaultKafkaProducerFactory<>(producerProps);
		return new KafkaTemplate<>(producerFactory);
	}
}
