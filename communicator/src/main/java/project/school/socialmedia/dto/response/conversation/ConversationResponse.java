package project.school.socialmedia.dto.response.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ConversationResponse {
  private String id;
  private String name;
  private List<String> memberIds;
}
