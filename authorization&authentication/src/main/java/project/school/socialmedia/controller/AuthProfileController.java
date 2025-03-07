package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.dto.response.Message;
import project.school.socialmedia.dto.response.UserResponse;
import project.school.socialmedia.service.ProfileService;
import project.school.socialmedia.service.TokenService;
import project.school.socialmedia.service.TokenServiceException;
import project.school.socialmedia.service.impl.ProfileServiceImpl;
import project.school.socialmedia.service.impl.TokenServiceImpl;
import project.school.socialmedia.util.RequestException;

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

  @GetMapping("/health-check")
  public Message healthCheck() {
    return new Message("OK");
  }

  @PatchMapping("/profile-update/{id}")
  public ResponseEntity<String> updateProfile(
          @RequestBody UpdateProfileRequest updateProfileRequest,
          @PathVariable String id) {
    try {
      setToken();
      String content = profileServiceImpl.updateUser(updateProfileRequest, id, clientToken);
      return ResponseEntity.ok(content);
    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
  }

  @GetMapping("/profile/{id}")
  public ResponseEntity<?> getProfile(@PathVariable String id) {
    try {
      setToken();
      UserResponse userResponse = profileServiceImpl.getUser(id, clientToken);
      return ResponseEntity.ok(userResponse);
    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).build();
    }catch (RequestException | JsonProcessingException e){
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
  }

  @DeleteMapping("/delete-profile/{id}")
  public ResponseEntity<String> deleteProfile(@PathVariable String id) {
    try {
      setToken();
      String content = profileServiceImpl.deleteUser(id, clientToken);
      return ResponseEntity.ok(content);
    } catch (ResponseStatusException e) {
      return ResponseEntity.status(e.getStatusCode()).body(e.getReason());
    }
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
