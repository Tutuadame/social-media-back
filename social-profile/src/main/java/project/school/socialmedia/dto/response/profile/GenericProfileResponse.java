package project.school.socialmedia.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class GenericProfileResponse {
  private String id;
  private String firstName;
  private String lastName;
  private String picture;
}
