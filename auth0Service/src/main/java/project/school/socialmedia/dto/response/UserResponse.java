package project.school.socialmedia.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

  @JsonProperty("user_id")
  private String userId;

  @JsonProperty("email")
  private String email;

  @JsonProperty("email_verified")
  private boolean emailVerified;

  @JsonProperty("name")
  private String name;

  @JsonProperty("nickname")
  private String nickname;

  @JsonProperty("picture")
  private String picture;

  @JsonProperty("update_at")
  private String updatedAt;

  @JsonProperty("last_ip")
  private String lastIp;

  @JsonProperty("last_login")
  private String lastLogin;

  @JsonProperty("logins_count")
  private int loginsCount;

  @JsonProperty("identities")
  private List<Map<String, Object>> identities;
}

