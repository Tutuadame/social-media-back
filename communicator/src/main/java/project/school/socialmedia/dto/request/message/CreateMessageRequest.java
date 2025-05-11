package project.school.socialmedia.dto.request.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMessageRequest {
  private long conversationId;
  private String content;
  private String senderId;
}
