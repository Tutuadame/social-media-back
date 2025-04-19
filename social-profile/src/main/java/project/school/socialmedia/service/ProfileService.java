package project.school.socialmedia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.school.socialmedia.dto.request.profile.CreateProfileRequest;
import project.school.socialmedia.dto.response.profile.GenericProfileResponse;
import project.school.socialmedia.dto.response.profile.UpdateIntroductionResponse;
import project.school.socialmedia.dto.response.profile.ProfileResponse;

public interface ProfileService {
  ProfileResponse createProfile(CreateProfileRequest createProfileRequest);
  UpdateIntroductionResponse updateIntroduction(String profileId, String introduction);
  void deleteProfile(String profileId);
  ProfileResponse getUserProfile(String profileId);
  Page<GenericProfileResponse> searchByName(String name, Pageable pageable);
}
