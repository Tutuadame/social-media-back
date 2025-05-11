package project.school.socialmedia.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeleteMemberFromConversationRequest {
  private String memberId;
  private long conversationId;
}