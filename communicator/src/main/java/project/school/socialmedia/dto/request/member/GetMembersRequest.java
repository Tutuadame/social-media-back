package project.school.socialmedia.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class GetMembersRequest {
  private int pageNumber;
  private int pageSize;
}
