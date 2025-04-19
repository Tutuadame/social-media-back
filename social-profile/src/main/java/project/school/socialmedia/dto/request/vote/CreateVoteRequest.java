package project.school.socialmedia.dto.request.vote;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateVoteRequest {

  private String profileId;
  private long postId;
  private boolean vote; //1 like 0 dislike
}
