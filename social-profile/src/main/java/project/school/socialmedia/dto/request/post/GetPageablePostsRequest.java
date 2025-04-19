package project.school.socialmedia.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetPageablePostsRequest {
  private int pageNumber;
  private int pageSize;
}