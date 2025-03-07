package project.school.socialmedia.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import project.school.socialmedia.dto.response.TokenResponse;
import project.school.socialmedia.dto.response.UserResponse;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class ServiceUtils {

  private final ObjectMapper objectMapper;

  @Autowired
  public ServiceUtils(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public Map<String, Object> createMap(String[] keys, Object[] values) {
    if (keys.length != values.length) {
      throw new IllegalArgumentException("Keys and values arrays must have the same length.");
    }
    return IntStream.range(0, keys.length)
            .boxed()
            .collect(Collectors.toMap(i -> keys[i], i -> values[i]));
  }

  public String convertToString(Map<String, Object> map) throws JsonProcessingException {
    return objectMapper.writeValueAsString(map);
  }

  public TokenResponse convertToTokenDTO(String content) throws JsonProcessingException {
    return objectMapper.readValue(content, TokenResponse.class);
  }

  public UserResponse convertToUserResponse(String auth0User) throws JsonProcessingException {
    return objectMapper.readValue(auth0User, UserResponse.class);
  }
}

