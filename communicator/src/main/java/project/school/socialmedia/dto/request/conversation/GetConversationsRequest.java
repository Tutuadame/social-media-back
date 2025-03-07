package project.school.socialmedia.dto.request.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetConversationsRequest {
  private int pageNumber;
  private int pageSize;
}
