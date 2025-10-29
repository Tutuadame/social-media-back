package project.school.socialprofile.dto.request.vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateVoteRequest {

  private String profileId;
  private long postId;
  private boolean vote;
}
