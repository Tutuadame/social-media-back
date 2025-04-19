package project.school.socialmedia.dto.response.connection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ConnectionResponse {
  private long id;
  private String profileId;
  private String firstName;
  private String lastName;
  private String picture;
}