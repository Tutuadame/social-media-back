package project.school.socialmedia.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateIntroductionResponse {
  private String profileId;
  private String introduction;
}
