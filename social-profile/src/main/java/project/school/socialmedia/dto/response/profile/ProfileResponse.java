package project.school.socialmedia.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileResponse {
  private String id;
  private String introduction;
  private String gender;
  private String firstName;
  private String lastName;
  private String picture;
}
