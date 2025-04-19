package project.school.socialmedia.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import project.school.socialmedia.NotificationHandlerApplication;
import project.school.socialmedia.dao.Notification;
import project.school.socialmedia.service.NotificationService;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
@AllArgsConstructor
@Slf4j
public class NotificationListener {

  private final NotificationService notificationService;

  private final ObjectMapper objectMapper;

  @KafkaListener(topics = "notification.created")
  public void listen(final String input) {
    log.info("Received Notification: {}", input);
    try {

      final Notification notification = objectMapper.readValue(input, Notification.class);

      final Notification savedNotification = notificationService.save(notification);

      log.info("Notification '{}' persisted!", savedNotification.getCreatedAt().toString());

    } catch(final JsonProcessingException ex) {
      log.error("Invalid message received: {}", input);
      log.error(ex.getMessage());
    }
  }
}