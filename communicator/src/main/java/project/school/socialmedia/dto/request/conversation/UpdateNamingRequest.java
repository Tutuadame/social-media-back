package project.school.socialmedia.dto.request.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateNamingRequest {
  private String name;
}