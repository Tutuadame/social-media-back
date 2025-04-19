package project.school.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import project.school.socialmedia.dto.NotificationResponse;
import project.school.socialmedia.service.NotificationService;

@RestController
@AllArgsConstructor
@RequestMapping("/notificationApi")
public class NotificationController {

  private NotificationService notificationService;

  @GetMapping("/notifications")
  public ResponseEntity<Page<NotificationResponse>> listNotifications(
          @RequestParam(defaultValue = "10") int pageSize,
          @RequestParam int pageNumber,
          @RequestParam String userId
  ){
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    return ResponseEntity.status(HttpStatus.OK).body(
            notificationService.listNotifications(pageable, userId)
    );
  }
}