package project.school.socialprofile.dto.request.connections;

import lombok.Data;

@Data
public class CreateConnectionRequest {
  private String initiatorId;
  private String targetId;
}
