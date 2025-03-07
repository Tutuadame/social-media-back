package project.school.socialmedia.dto.request.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GetMessagesRequest {
  private int pageNumber;
  private int pageSize;
}
