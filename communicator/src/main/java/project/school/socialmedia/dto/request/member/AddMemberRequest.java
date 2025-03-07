package project.school.socialmedia.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AddMemberRequest {
  private String memberId;
  private long conversationId;
}
