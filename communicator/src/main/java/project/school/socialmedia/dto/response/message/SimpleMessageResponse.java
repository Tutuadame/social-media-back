package project.school.socialmedia.dto.response.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SimpleMessageResponse {
  private long id;
  private String content;
  private String sentAt;
}

