package project.school.socialmedia.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic messageSentToTopic(final KafkaConfigProps kafkaConfigProps) {
    return TopicBuilder.name(kafkaConfigProps.getTopic())
            .partitions(1)
            .replicas(1)
            .build();
  }
}
