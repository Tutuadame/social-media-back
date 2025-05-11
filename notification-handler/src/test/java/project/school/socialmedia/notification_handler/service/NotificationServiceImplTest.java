package project.school.socialmedia.notification_handler.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.school.socialmedia.domain.Notification;
import project.school.socialmedia.dto.NotificationResponse;
import project.school.socialmedia.repository.NotificationRepository;
import project.school.socialmedia.service.impl.NotificationServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationServiceImplTest {

  @Mock
  private NotificationRepository notificationRepository;

  @InjectMocks
  private NotificationServiceImpl notificationService;

  private Notification testNotification;
  private LocalDateTime testTime;
  private final Pageable pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    testTime = LocalDateTime.of(2023, 1, 1, 12, 0, 0);

    // Setup test notification
    testNotification = Notification.builder()
            .id(1L)
            .message("New message from John")
            .userIds(Arrays.asList("user123", "user456"))
            .createdAt(testTime)
            .build();
  }

  @Test
  void save_ValidNotification_ReturnsSavedNotification() {
    // Arrange
    when(notificationRepository.save(any(Notification.class))).thenReturn(testNotification);

    // Act
    Notification result = notificationService.save(testNotification);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("New message from John", result.getMessage());
    assertEquals(testTime, result.getCreatedAt());
    assertEquals(2, result.getUserIds().size());
    assertEquals("user123", result.getUserIds().get(0));
    assertEquals("user456", result.getUserIds().get(1));

    verify(notificationRepository).save(testNotification);
  }

  @Test
  void listNotifications_ForUser_ReturnsNotificationPage() {
    // Arrange
    String userId = "user123";
    List<Notification> notifications = Collections.singletonList(testNotification);
    Page<Notification> notificationPage = new PageImpl<>(notifications);

    when(notificationRepository.findByUserId(pageable, userId)).thenReturn(notificationPage);

    // Act
    Page<NotificationResponse> result = notificationService.listNotifications(pageable, userId);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("New message from John", result.getContent().get(0).getMessage());
    assertEquals(testTime.toString(), result.getContent().get(0).getCreatedAt());

    verify(notificationRepository).findByUserId(pageable, userId);
  }

  @Test
  void listNotifications_NoNotifications_ReturnsEmptyPage() {
    // Arrange
    String userId = "user789";
    Page<Notification> emptyPage = new PageImpl<>(Collections.emptyList());

    when(notificationRepository.findByUserId(pageable, userId)).thenReturn(emptyPage);

    // Act
    Page<NotificationResponse> result = notificationService.listNotifications(pageable, userId);

    // Assert
    assertNotNull(result);
    assertEquals(0, result.getContent().size());
    assertEquals(true, result.getContent().isEmpty());

    verify(notificationRepository).findByUserId(pageable, userId);
  }

  @Test
  void listNotifications_MultiplePagesOfNotifications_ReturnsCorrectPage() {
    // Arrange
    String userId = "user123";
    Pageable customPageable = PageRequest.of(1, 2); // Second page, 2 items per page

    LocalDateTime time1 = LocalDateTime.of(2023, 1, 1, 12, 0, 0);
    LocalDateTime time2 = LocalDateTime.of(2023, 1, 1, 11, 0, 0);

    Notification notification1 = Notification.builder()
            .id(2L)
            .message("Second page notification 1")
            .userIds(Collections.singletonList(userId))
            .createdAt(time1)
            .build();

    Notification notification2 = Notification.builder()
            .id(3L)
            .message("Second page notification 2")
            .userIds(Collections.singletonList(userId))
            .createdAt(time2)
            .build();

    List<Notification> secondPageNotifications = Arrays.asList(notification1, notification2);
    Page<Notification> secondPageNotificationPage = new PageImpl<>(
            secondPageNotifications,
            customPageable,
            6 // Total 6 elements across all pages
    );

    when(notificationRepository.findByUserId(customPageable, userId)).thenReturn(secondPageNotificationPage);

    // Act
    Page<NotificationResponse> result = notificationService.listNotifications(customPageable, userId);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    assertEquals(6, result.getTotalElements());
    assertEquals(3, result.getTotalPages());
    assertEquals(1, result.getNumber()); // Page number (0-based)
    assertEquals("Second page notification 1", result.getContent().get(0).getMessage());
    assertEquals("Second page notification 2", result.getContent().get(1).getMessage());

    verify(notificationRepository).findByUserId(customPageable, userId);
  }
}