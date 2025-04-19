package project.school.socialmedia.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProfileRequest {

  private String key;
  private String value; //Not good for different update values (boolean)
}
