package project.school.socialprofile.dto.response.profile;

import lombok.AllArgsConstructor;
import lombok.Data;
import project.school.socialprofile.domain.Profile;

@AllArgsConstructor
@Data
public class BasicProfileResponse {
  private String id;
  private String firstName;
  private String lastName;
  private String picture;

  public BasicProfileResponse(Profile profile) {
    this.id = profile.getId();
    this.firstName = profile.getFirstName();
    this.lastName = profile.getLastName();
    this.picture = profile.getPicture();
  }
}
