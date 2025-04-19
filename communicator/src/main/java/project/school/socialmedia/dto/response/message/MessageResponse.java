package project.school.socialmedia.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageResponse {
  private long id;
  private String senderId;
  private String content;
  private String sentAt;
}
