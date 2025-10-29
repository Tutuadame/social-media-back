package project.school.socialprofile.dto.response.vote;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CheckVoteResponse {
  private String type; //like, dislike, none
}
