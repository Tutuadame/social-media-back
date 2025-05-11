package project.school.socialmedia.dto.request.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateMessageRequest {
  private String messageContent;
}
