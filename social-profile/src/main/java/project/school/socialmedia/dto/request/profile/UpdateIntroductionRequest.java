package project.school.socialmedia.dto.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateIntroductionRequest {
  private String introduction;
}
