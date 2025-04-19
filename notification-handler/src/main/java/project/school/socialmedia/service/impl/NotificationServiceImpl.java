package project.school.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.school.socialmedia.dao.Notification;
import project.school.socialmedia.dto.NotificationResponse;
import project.school.socialmedia.repository.NotificationRepository;
import project.school.socialmedia.service.NotificationService;

@Service
@AllArgsConstructor
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;

  @Override
  public Notification save(Notification notification) {
    return notificationRepository.save(notification);
  }

  @Override
  public Page<NotificationResponse> listNotifications(Pageable pageable, String userId) {
    Page<Notification> notificationPage = notificationRepository.findByUserId(pageable, userId);
    return notificationPage.map(notification -> new NotificationResponse(notification.getMessage(), notification.getCreatedAt().toString()));
  }
}