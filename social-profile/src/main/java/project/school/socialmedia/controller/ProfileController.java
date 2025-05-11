package project.school.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.dto.request.profile.CreateProfileRequest;
import project.school.socialmedia.dto.request.profile.UpdateIntroductionRequest;
import project.school.socialmedia.dto.response.profile.GenericProfileResponse;
import project.school.socialmedia.dto.response.profile.ProfileResponse;
import project.school.socialmedia.dto.response.profile.UpdateIntroductionResponse;
import project.school.socialmedia.service.ProfileService;

@RestController
@AllArgsConstructor
@RequestMapping("/profileApi")
public class ProfileController {

  private final ProfileService profileService;

  @PostMapping("/new")
  public ResponseEntity<ProfileResponse> createProfile (
          @RequestBody CreateProfileRequest createProfileRequest
  ) {
    ProfileResponse profileResponse = profileService.createProfile(createProfileRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
  }

  @DeleteMapping("/{profileId}")
  public ResponseEntity<String> deleteProfile (
          @PathVariable String profileId
  ) {
    profileService.deleteProfile(profileId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Profile deleted!");
  }

  @GetMapping("/{profileId}")
  public ResponseEntity<ProfileResponse> getProfile (@PathVariable String profileId) {
    ProfileResponse profileResponse = profileService.getUserProfile(profileId);
    return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
  }

  @PatchMapping("/introduction/{profileId}")
  public ResponseEntity<UpdateIntroductionResponse> updateIntroduction (@PathVariable String profileId, @RequestBody UpdateIntroductionRequest updateIntroductionRequest) {
    UpdateIntroductionResponse updateIntroductionResponse = profileService.updateIntroduction(profileId, updateIntroductionRequest.getIntroduction());
    return ResponseEntity.status(HttpStatus.OK).body(updateIntroductionResponse);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<GenericProfileResponse>> searchProfileByName(
          @RequestParam String name,
          @RequestParam int pageNumber,
          @RequestParam int pageSize
  ){
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<GenericProfileResponse> genericProfileResponses = profileService.searchByName(name, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(genericProfileResponses);
  }
}