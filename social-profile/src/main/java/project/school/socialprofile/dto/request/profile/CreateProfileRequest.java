package project.school.socialprofile.dto.request.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProfileRequest {
  private String profileId;
  private String gender;
  private String firstName;
  private String lastName;
}
