package project.school.socialmedia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.school.socialmedia.dao.Notification;
import project.school.socialmedia.dto.NotificationResponse;

public interface NotificationService {

  Notification save(Notification notification);

  Page<NotificationResponse> listNotifications(Pageable pageable, String userId);
}