package project.school.socialmedia.dto.request.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateMessageRequest {
  private String messageContent;
}
