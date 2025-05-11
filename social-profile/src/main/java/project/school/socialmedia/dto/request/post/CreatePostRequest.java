package project.school.socialmedia.dto.request.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePostRequest {
  private String profileId;
  private String content;
}
