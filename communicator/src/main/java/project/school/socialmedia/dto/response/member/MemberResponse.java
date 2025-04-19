package project.school.socialmedia.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MemberResponse {
  private String id;
  private String firstName;
  private String lastName;
  private String picture;
}
