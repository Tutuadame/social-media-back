package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.dto.response.UserResponse;
import project.school.socialmedia.service.ProfileService;
import project.school.socialmedia.service.TokenService;
import project.school.socialmedia.service.TokenServiceException;
import project.school.socialmedia.service.impl.ProfileServiceImpl;
import project.school.socialmedia.service.impl.TokenServiceImpl;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "https://social.media:3000")
public class AuthProfileController {

  private final ProfileService profileServiceImpl;
  private final TokenService tokenServiceImpl;
  private String clientToken = "";

  @Autowired
  public AuthProfileController(ProfileServiceImpl profileServiceImpl, TokenServiceImpl tokenServiceImpl) {
    this.profileServiceImpl = profileServiceImpl;
    this.tokenServiceImpl = tokenServiceImpl;
  }

  @PatchMapping("/profile-update/{id}")
  public ResponseEntity<String> updateProfile(
          @RequestBody UpdateProfileRequest updateProfileRequest,
          @PathVariable String id
  ) throws JsonProcessingException {
    setToken();
    String responseBody = profileServiceImpl.updateUser(updateProfileRequest, id, clientToken);
    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

  @GetMapping("/profile/{id}")
  public ResponseEntity<UserResponse> getProfile(@PathVariable String id) throws JsonProcessingException {
    setToken();
    UserResponse userResponse = profileServiceImpl.getUser(id, clientToken);
    return ResponseEntity.ok(userResponse);
  }

  @DeleteMapping("/delete-profile/{id}")
  public ResponseEntity<String> deleteProfile(@PathVariable String id) {
    setToken();
    String responseBody = profileServiceImpl.deleteUser(id, clientToken);
    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

  private void setToken() throws ResponseStatusException {
    try {
      if (tokenServiceImpl.isTokenSet(clientToken) || tokenServiceImpl.isTokenExpired(clientToken)) {
        clientToken = tokenServiceImpl.getManagementToken();
      }
    } catch (TokenServiceException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to retrieve management token", e);
    }
  }
}
