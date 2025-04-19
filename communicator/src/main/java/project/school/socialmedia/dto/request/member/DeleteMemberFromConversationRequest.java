package project.school.socialmedia.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DeleteMemberFromConversationRequest {
  private String memberId;
  private long conversationId;
}