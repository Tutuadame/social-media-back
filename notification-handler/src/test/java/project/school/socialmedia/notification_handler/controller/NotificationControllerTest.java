package project.school.socialmedia.notification_handler.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.controller.NotificationController;
import project.school.socialmedia.dto.NotificationResponse;
import project.school.socialmedia.service.NotificationService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class NotificationControllerTest {

  @Mock
  private NotificationService notificationService;

  @InjectMocks
  private NotificationController notificationController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void listNotifications_ReturnsNotificationPage() {
    // Arrange
    int pageSize = 10;
    int pageNumber = 0;
    String userId = "user123";

    List<NotificationResponse> notifications = Arrays.asList(
            new NotificationResponse("New message from John", "2023-01-01T12:00:00"),
            new NotificationResponse("John liked your post", "2023-01-01T11:30:00")
    );

    Page<NotificationResponse> notificationPage = new PageImpl<>(notifications);

    when(notificationService.listNotifications(any(Pageable.class), eq(userId)))
            .thenReturn(notificationPage);

    // Act
    ResponseEntity<Page<NotificationResponse>> response =
            notificationController.listNotifications(pageSize, pageNumber, userId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<NotificationResponse> result = response.getBody();
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    assertEquals("New message from John", result.getContent().get(0).getMessage());
    assertEquals("John liked your post", result.getContent().get(1).getMessage());

    // Verify that the service was called with the correct parameters
    Pageable expectedPageable = PageRequest.of(pageNumber, pageSize);
    verify(notificationService).listNotifications(eq(expectedPageable), eq(userId));
  }

  @Test
  void listNotifications_WithCustomPageSize_UsesCorrectPageable() {
    // Arrange
    int customPageSize = 5;
    int pageNumber = 1;
    String userId = "user123";

    List<NotificationResponse> notifications = Arrays.asList(
            new NotificationResponse("New message from Jane", "2023-01-01T10:00:00")
    );

    Page<NotificationResponse> notificationPage = new PageImpl<>(notifications);

    when(notificationService.listNotifications(any(Pageable.class), eq(userId)))
            .thenReturn(notificationPage);

    // Act
    ResponseEntity<Page<NotificationResponse>> response =
            notificationController.listNotifications(customPageSize, pageNumber, userId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // Verify that the service was called with the correct custom page size
    Pageable expectedPageable = PageRequest.of(pageNumber, customPageSize);
    verify(notificationService).listNotifications(eq(expectedPageable), eq(userId));
  }

  @Test
  void listNotifications_WithEmptyResults_ReturnsEmptyPage() {
    // Arrange
    int pageSize = 10;
    int pageNumber = 0;
    String userId = "user123";

    List<NotificationResponse> emptyList = List.of();
    Page<NotificationResponse> emptyPage = new PageImpl<>(emptyList);

    when(notificationService.listNotifications(any(Pageable.class), eq(userId)))
            .thenReturn(emptyPage);

    // Act
    ResponseEntity<Page<NotificationResponse>> response =
            notificationController.listNotifications(pageSize, pageNumber, userId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<NotificationResponse> result = response.getBody();
    assertNotNull(result);
    assertEquals(0, result.getContent().size());
    assertEquals(true, result.getContent().isEmpty());

    // Verify that the service was called with the correct parameters
    Pageable expectedPageable = PageRequest.of(pageNumber, pageSize);
    verify(notificationService).listNotifications(eq(expectedPageable), eq(userId));
  }
}
