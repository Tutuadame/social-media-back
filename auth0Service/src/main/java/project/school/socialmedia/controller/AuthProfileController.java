package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.service.ProfileService;
import project.school.socialmedia.service.TokenService;
import project.school.socialmedia.exception.TokenServiceException;
import project.school.socialmedia.service.impl.ProfileServiceImpl;
import project.school.socialmedia.service.impl.TokenServiceImpl;

@RestController
@RequestMapping("/api")
public class AuthProfileController {

  private final ProfileService profileService;
  private final TokenService tokenService;
  private String clientToken = "";

  @Autowired
  public AuthProfileController(ProfileServiceImpl profileService, TokenServiceImpl tokenService) {
    this.profileService = profileService;
    this.tokenService = tokenService;
  }

  @PatchMapping("/profile-update/{id}")
  public ResponseEntity<String> updateProfile(
          @RequestBody(required = true) UpdateProfileRequest updateProfileRequest,
          @PathVariable(name = "id") String id
  ) throws JsonProcessingException {
    setToken();
    String responseBody = profileService.updateUser(updateProfileRequest, id, clientToken);
    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

  @DeleteMapping("/delete-profile/{id}")
  public ResponseEntity<String> deleteProfile(@PathVariable(name = "id") String id) {
    setToken();
    String responseBody = profileService.deleteUser(id, clientToken);
    return ResponseEntity.status(HttpStatus.OK).body(responseBody);
  }

  private void setToken() throws ResponseStatusException {
    try {
      if (tokenService.isTokenSet(clientToken) || tokenService.isTokenExpired(clientToken)) {
        clientToken = tokenService.getManagementToken();
      }
    } catch (TokenServiceException e) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to retrieve management token", e);
    }
  }
}
