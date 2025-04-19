package project.school.socialmedia.dto.response.member;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckConnectionsBatchResponse {
  private List<String> connectedIds;
}

