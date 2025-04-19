package project.school.socialmedia.dto.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateProfileRequest {
  private String profileId;
  private String gender;
  private String firstName;
  private String lastName;
}
