package project.school.socialmedia.dto.request.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateConversationRequest {
  private String[] members;
  private String name;
}
