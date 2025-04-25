package batch.springkafkapracticetest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class SpringKafkaPracticeTestApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringKafkaPracticeTestApplication.class, args);
	}

}
