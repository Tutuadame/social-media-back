package project.school.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.school.socialmedia.domain.GenderEnum;
import project.school.socialmedia.domain.Profile;
import project.school.socialmedia.dto.request.profile.CreateProfileRequest;
import project.school.socialmedia.dto.response.profile.GenericProfileResponse;
import project.school.socialmedia.dto.response.profile.ProfileResponse;
import project.school.socialmedia.dto.response.profile.UpdateIntroductionResponse;
import project.school.socialmedia.repository.ProfileRepository;
import project.school.socialmedia.service.ProfileService;

import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ProfileServiceImpl implements ProfileService {

  private final ProfileRepository profileRepository;

  @Override
  public ProfileResponse createProfile(CreateProfileRequest createProfileRequest) {
    Profile profile = getProfile(createProfileRequest);
    if (profileRepository.existsById(profile.getId())) {
      throw new RuntimeException("Profile already exists for this user ID.");
    }
    profileRepository.save(profile);

    return new ProfileResponse(
            profile.getId(),
            profile.getIntroduction(),
            profile.getGender().toString(),
            profile.getFirstName(),
            profile.getLastName(),
            profile.getPicture()
    );
  }

  private Profile getProfile(CreateProfileRequest createProfileRequest) {
    GenderEnum genderEnum = checkGender(createProfileRequest.getGender());
    String defaultAvatar = "https://avatar.iran.liara.run/username?username=" + createProfileRequest.getFirstName() + "+" + createProfileRequest.getLastName();
    return new Profile(
            createProfileRequest.getProfileId(),
            defaultAvatar,
            genderEnum,
            createProfileRequest.getFirstName(),
            createProfileRequest.getLastName(),
            "Say something about yourself!"
    );
  }

  @Override
  public UpdateIntroductionResponse updateIntroduction(String profileId, String introduction) {
    Profile profile = profileRepository.findById(profileId).orElseThrow(NoSuchElementException::new);
    profile.setIntroduction(introduction);
    profile = profileRepository.save(profile);
    return new UpdateIntroductionResponse(profile.getId(), profile.getIntroduction());
  }

  @Override
  public void deleteProfile(String profileId) {
    if (profileRepository.existsById(profileId)) {
      profileRepository.deleteById(profileId);
    } else {
      System.out.println("Profile with ID "+profileId+" does not exist or was already deleted.");
    }
  }


  @Override
  public ProfileResponse getUserProfile(String profileId) {
    Profile profile = profileRepository.findById(profileId).orElseThrow(NoSuchElementException::new);
    return new ProfileResponse(
            profile.getId(),
            profile.getIntroduction(),
            profile.getGender().toString(),
            profile.getFirstName(),
            profile.getLastName(),
            profile.getPicture()
    );
  }

  @Override
  public Page<GenericProfileResponse> searchByName(String name, Pageable pageable) {
    Page<Profile> profiles = profileRepository.searchByName(name, pageable);
    return profiles.map(
            profile -> new GenericProfileResponse(profile.getId(), profile.getFirstName(), profile.getLastName(), profile.getPicture())
    );
  }

  private GenderEnum checkGender(String gender) {
    return switch (gender) {
      case "Male" -> GenderEnum.MALE;
      case "Female" -> GenderEnum.FEMALE;
      default -> GenderEnum.OTHER;
    };
  }
}
