package project.school.socialmedia.dto.request.member;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CheckConnectionsBatchRequest {
  private String requesterId;
  private List<String> targetIds;
}

