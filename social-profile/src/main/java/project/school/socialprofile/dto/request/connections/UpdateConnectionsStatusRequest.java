package project.school.socialprofile.dto.request.connections;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateConnectionsStatusRequest {
  private long id;
  private String status;
}
