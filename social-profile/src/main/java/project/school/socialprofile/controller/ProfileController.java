package project.school.socialprofile.controller;

import lombok.AllArgsConstructor;
import project.school.socialprofile.dto.request.profile.CreateProfileRequest;
import project.school.socialprofile.dto.request.profile.UpdateIntroductionRequest;
import project.school.socialprofile.dto.response.profile.BasicProfileResponse;
import project.school.socialprofile.dto.response.profile.ProfileResponse;
import project.school.socialprofile.dto.response.profile.UpdateIntroductionResponse;
import project.school.socialprofile.service.ProfileService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/profileApi")
public class ProfileController {

  private final ProfileService profileService;

  @PostMapping("/new")
  public ResponseEntity<ProfileResponse> createProfile (
          @RequestBody(required = true) CreateProfileRequest createProfileRequest
  ) {
    ProfileResponse profileResponse = profileService.createProfile(createProfileRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(profileResponse);
  }

  @DeleteMapping("/{profileId}")
  public ResponseEntity<String> deleteProfile (
          @PathVariable(name = "profileId") String profileId
  ) {
    profileService.deleteProfile(profileId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Profile deleted!");
  }

  @GetMapping("/{profileId}")
  public ResponseEntity<ProfileResponse> getProfile (@PathVariable(name = "profileId") String profileId) {
    ProfileResponse profileResponse = profileService.getUserProfile(profileId);
    return ResponseEntity.status(HttpStatus.OK).body(profileResponse);
  }

  @PatchMapping("/introduction/{profileId}")
  public ResponseEntity<UpdateIntroductionResponse> updateIntroduction (
          @PathVariable(name = "profileId") String profileId,
          @RequestBody(required = true) UpdateIntroductionRequest updateIntroductionRequest
  ) {
    UpdateIntroductionResponse updateIntroductionResponse = profileService.updateIntroduction(profileId, updateIntroductionRequest.getIntroduction());
    return ResponseEntity.status(HttpStatus.OK).body(updateIntroductionResponse);
  }

  @GetMapping("/search")
  public ResponseEntity<Page<BasicProfileResponse>> searchProfileByName(
          @RequestParam("name") String name,
          @RequestParam("pageNumber") int pageNumber,
          @RequestParam("pageSize") int pageSize
  ){
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<BasicProfileResponse> basicProfileResponses = profileService.searchByName(name, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(basicProfileResponses);
  }
}