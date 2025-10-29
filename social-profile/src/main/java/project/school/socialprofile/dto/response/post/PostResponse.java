package project.school.socialprofile.dto.response.post;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import project.school.socialprofile.domain.Post;
import project.school.socialprofile.dto.response.profile.BasicProfileResponse;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
  private Long id;
  private BasicProfileResponse basicProfileResponse;
  private String content;
  private LocalDateTime createdAt;
  private int likes; //Calculated
  private int dislikes; //Calculated

  public PostResponse(Post post, BasicProfileResponse basicProfileResponse, int likes, int dislikes) {
    this.id = post.getId();
    this.content = post.getContent();
    this.createdAt = post.getCreatedAt();
    this.likes = likes;
    this.dislikes = dislikes;
    this.basicProfileResponse = basicProfileResponse;
  }
}
