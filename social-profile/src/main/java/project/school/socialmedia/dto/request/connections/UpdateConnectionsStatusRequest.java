package project.school.socialmedia.dto.request.connections;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateConnectionsStatusRequest {
  private long id;
  private String status;
}
