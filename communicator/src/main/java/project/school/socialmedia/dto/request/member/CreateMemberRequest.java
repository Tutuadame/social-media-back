package project.school.socialmedia.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateMemberRequest {
  private String memberId;
  private String firstName;
  private String lastName;
}
