package project.school.socialmedia.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {

  @Bean
  public NewTopic booksPublishedTopic(final project.school.socialmedia.config.KafkaConfigProps kafkaConfigProps) {
    return TopicBuilder.name(kafkaConfigProps.getTopic())
            .partitions(1)
            .replicas(1)
            .build();
  }
}
