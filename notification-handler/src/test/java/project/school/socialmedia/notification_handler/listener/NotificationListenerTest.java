package project.school.socialmedia.notification_handler.listener;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import project.school.socialmedia.domain.Notification;
import project.school.socialmedia.listener.NotificationListener;
import project.school.socialmedia.service.NotificationService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationListenerTest {

  @Mock
  private NotificationService notificationService;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private NotificationListener notificationListener;

  private Notification testNotification;
  private String validJson;

  @BeforeEach
  void setUp() throws JsonProcessingException {
    MockitoAnnotations.openMocks(this);

    // Setup test data
    LocalDateTime testTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    List<String> userIds = Arrays.asList("user123", "user456");

    testNotification = Notification.builder()
            .id(1L)
            .message("New message from John")
            .userIds(userIds)
            .createdAt(testTime)
            .build();

    validJson = "{\"id\":1,\"userIds\":[\"user123\",\"user456\"],\"message\":\"New message from John\",\"createdAt\":\"2023-01-01T12:00:00\"}";

    // Setup mock behavior
    when(objectMapper.readValue(validJson, Notification.class)).thenReturn(testNotification);
    when(notificationService.save(any(Notification.class))).thenReturn(testNotification);
  }

  @Test
  void listen_ValidJson_SavesNotification() throws JsonProcessingException {
    // Act
    notificationListener.listen(validJson);

    // Assert
    verify(objectMapper).readValue(validJson, Notification.class);
    verify(notificationService).save(testNotification);
  }

  @Test
  void listen_InvalidJson_LogsErrorWithoutSaving() throws JsonProcessingException {
    // Arrange
    String invalidJson = "{malformed json}";
    when(objectMapper.readValue(invalidJson, Notification.class))
            .thenThrow(new JsonProcessingException("Invalid JSON") {});

    // Act
    notificationListener.listen(invalidJson);

    // Assert
    verify(objectMapper).readValue(invalidJson, Notification.class);
    verify(notificationService, never()).save(any(Notification.class));
  }
}
