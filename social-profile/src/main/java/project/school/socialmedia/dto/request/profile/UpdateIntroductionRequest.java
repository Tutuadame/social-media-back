package project.school.socialmedia.dto.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateIntroductionRequest {
  private String introduction;
}
