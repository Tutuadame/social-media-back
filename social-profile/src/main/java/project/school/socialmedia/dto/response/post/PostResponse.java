package project.school.socialmedia.dto.response.post;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.school.socialmedia.domain.Post;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
  private Long id;
  private String profileId;
  private String content;
  private LocalDateTime createdAt;
  private int likes; //Calculated
  private int dislikes; //Calculated

  public PostResponse(Post post, int likes, int dislikes) {
    this.id = post.getId();
    this.profileId = post.getProfile().getId();
    this.content = post.getContent();
    this.createdAt = post.getCreatedAt();
    this.likes = likes;
    this.dislikes = dislikes;
  }
}
