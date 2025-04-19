package project.school.socialmedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.dto.response.UserResponse;

public interface ProfileService {

  String updateUser(UpdateProfileRequest updateProfileRequest, String userId, String accessToken) throws JsonProcessingException;

  UserResponse getUser(String userId, String accessToken) throws JsonProcessingException;

  String deleteUser(String userId, String accessToken);
}
