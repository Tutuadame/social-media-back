package project.school.socialprofile.dto.request.connections;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckConnectionsBatchRequest {
  private String requesterId;
  private List<String> targetIds;
}

