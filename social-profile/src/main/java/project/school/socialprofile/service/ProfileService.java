package project.school.socialprofile.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.school.socialprofile.dto.request.profile.CreateProfileRequest;
import project.school.socialprofile.dto.response.profile.BasicProfileResponse;
import project.school.socialprofile.dto.response.profile.ProfileResponse;
import project.school.socialprofile.dto.response.profile.UpdateIntroductionResponse;

public interface ProfileService {
  ProfileResponse createProfile(CreateProfileRequest createProfileRequest);
  UpdateIntroductionResponse updateIntroduction(String profileId, String introduction);
  void deleteProfile(String profileId);
  ProfileResponse getUserProfile(String profileId);
  Page<BasicProfileResponse> searchByName(String name, Pageable pageable);
}
