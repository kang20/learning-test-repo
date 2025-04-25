package batch.springkafkapracticetest.config.admin;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.common.config.TopicConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaAdmin;
import org.springframework.stereotype.Component;

@Component
public class Topic1Replicas {
	@Autowired
	private KafkaAdmin kafkaAdmin;

	@Value("${test.topic.name}")
	private String TOPIC_NAME;

	private static final int PARTITION_COUNT_ONE = 1;

	@Bean
	public NewTopic topic1() {
		return TopicBuilder.name(TOPIC_NAME)
			.partitions(PARTITION_COUNT_ONE)
			.replicas(1)
			.config(TopicConfig.RETENTION_MS_CONFIG, "8640000") // 1일 유지
			.build();
	}

}
