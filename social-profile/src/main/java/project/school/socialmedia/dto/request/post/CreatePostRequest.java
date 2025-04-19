package project.school.socialmedia.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreatePostRequest {
  private String profileId;
  private String content;
}
