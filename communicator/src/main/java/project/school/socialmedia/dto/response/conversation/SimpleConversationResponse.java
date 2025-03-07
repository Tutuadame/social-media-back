package project.school.socialmedia.dto.response.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimpleConversationResponse {
  private long id;
  private String name;
}