package project.school.socialmedia.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "notifications.kafka")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class KafkaConfigProps {
  private String topic;
}