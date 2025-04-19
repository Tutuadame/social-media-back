package project.school.socialmedia.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class NotificationResponse {
  private String message;
  private String createdAt;
}
