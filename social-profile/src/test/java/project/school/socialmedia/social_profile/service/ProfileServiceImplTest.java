package project.school.socialmedia.social_profile.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.school.socialmedia.domain.GenderEnum;
import project.school.socialmedia.domain.Profile;
import project.school.socialmedia.dto.request.profile.CreateProfileRequest;
import project.school.socialmedia.dto.response.profile.GenericProfileResponse;
import project.school.socialmedia.dto.response.profile.ProfileResponse;
import project.school.socialmedia.dto.response.profile.UpdateIntroductionResponse;
import project.school.socialmedia.repository.ProfileRepository;
import project.school.socialmedia.service.impl.ProfileServiceImpl;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProfileServiceImplTest {

  @Mock
  private ProfileRepository profileRepository;

  @InjectMocks
  private ProfileServiceImpl profileService;

  private Profile testProfile;
  private final Pageable pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Setup test profile
    testProfile = new Profile(
            "user123",
            "avatar.jpg",
            GenderEnum.MALE,
            "John",
            "Doe",
            "This is my introduction"
    );
  }

  @Test
  void createProfile_ValidRequest_ReturnsProfileResponse() {
    // Arrange
    CreateProfileRequest request = new CreateProfileRequest();
    request.setProfileId("user123");
    request.setFirstName("John");
    request.setLastName("Doe");
    request.setGender("Male");

    when(profileRepository.existsById("user123")).thenReturn(false);
    when(profileRepository.save(any(Profile.class))).thenReturn(testProfile);

    // Act
    ProfileResponse result = profileService.createProfile(request);

    // Assert
    assertNotNull(result);
    assertEquals("user123", result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("Male", result.getGender());

    verify(profileRepository).existsById("user123");
    verify(profileRepository).save(any(Profile.class));
  }

  @Test
  void createProfile_ProfileAlreadyExists_ThrowsException() {
    // Arrange
    CreateProfileRequest request = new CreateProfileRequest();
    request.setProfileId("user123");
    request.setFirstName("John");
    request.setLastName("Doe");
    request.setGender("Male");

    when(profileRepository.existsById("user123")).thenReturn(true);

    // Act & Assert
    assertThrows(RuntimeException.class, () -> profileService.createProfile(request));
    verify(profileRepository).existsById("user123");
    verify(profileRepository, never()).save(any(Profile.class));
  }

  @Test
  void updateIntroduction_ValidRequest_ReturnsUpdatedResponse() {
    // Arrange
    String profileId = "user123";
    String newIntroduction = "This is my updated introduction";

    Profile updatedProfile = new Profile(
            "user123",
            "avatar.jpg",
            GenderEnum.MALE,
            "John",
            "Doe",
            newIntroduction
    );

    when(profileRepository.findById(profileId)).thenReturn(Optional.of(testProfile));
    when(profileRepository.save(any(Profile.class))).thenReturn(updatedProfile);

    // Act
    UpdateIntroductionResponse result = profileService.updateIntroduction(profileId, newIntroduction);

    // Assert
    assertNotNull(result);
    assertEquals("user123", result.getProfileId());
    assertEquals(newIntroduction, result.getIntroduction());

    verify(profileRepository).findById(profileId);
    verify(profileRepository).save(testProfile);
  }

  @Test
  void updateIntroduction_ProfileNotFound_ThrowsException() {
    // Arrange
    String profileId = "nonexistent";
    String newIntroduction = "This is my updated introduction";

    when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () ->
            profileService.updateIntroduction(profileId, newIntroduction));
    verify(profileRepository).findById(profileId);
    verify(profileRepository, never()).save(any(Profile.class));
  }

  @Test
  void deleteProfile_ExistingProfile_DeletesProfile() {
    // Arrange
    String profileId = "user123";
    when(profileRepository.existsById(profileId)).thenReturn(true);
    doNothing().when(profileRepository).deleteById(profileId);

    // Act
    profileService.deleteProfile(profileId);

    // Assert
    verify(profileRepository).existsById(profileId);
    verify(profileRepository).deleteById(profileId);
  }

  @Test
  void deleteProfile_NonExistingProfile_LogsMessage() {
    // Arrange
    String profileId = "nonexistent";
    when(profileRepository.existsById(profileId)).thenReturn(false);

    // Act
    profileService.deleteProfile(profileId);

    // Assert
    verify(profileRepository).existsById(profileId);
    verify(profileRepository, never()).deleteById(anyString());
  }

  @Test
  void getUserProfile_ExistingProfile_ReturnsProfileResponse() {
    // Arrange
    String profileId = "user123";
    when(profileRepository.findById(profileId)).thenReturn(Optional.of(testProfile));

    // Act
    ProfileResponse result = profileService.getUserProfile(profileId);

    // Assert
    assertNotNull(result);
    assertEquals("user123", result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("Male", result.getGender());
    assertEquals("This is my introduction", result.getIntroduction());

    verify(profileRepository).findById(profileId);
  }

  @Test
  void getUserProfile_NonExistingProfile_ThrowsException() {
    // Arrange
    String profileId = "nonexistent";
    when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> profileService.getUserProfile(profileId));
    verify(profileRepository).findById(profileId);
  }

  @Test
  void searchByName_ReturnsMatchingProfiles() {
    // Arrange
    String name = "John";

    Profile profile1 = new Profile("user123", "avatar1.jpg", GenderEnum.MALE, "John", "Doe", "Introduction 1");
    Profile profile2 = new Profile("user456", "avatar2.jpg", GenderEnum.MALE, "John", "Smith", "Introduction 2");

    List<Profile> profiles = Arrays.asList(profile1, profile2);
    Page<Profile> profilePage = new PageImpl<>(profiles);

    when(profileRepository.searchByName(eq(name), any(Pageable.class))).thenReturn(profilePage);

    // Act
    Page<GenericProfileResponse> results = profileService.searchByName(name, pageable);

    // Assert
    assertNotNull(results);
    assertEquals(2, results.getContent().size());
    assertEquals("user123", results.getContent().getFirst().getId());
    assertEquals("John", results.getContent().get(0).getFirstName());
    assertEquals("Doe", results.getContent().get(0).getLastName());
    assertEquals("user456", results.getContent().get(1).getId());
    assertEquals("Smith", results.getContent().get(1).getLastName());

    verify(profileRepository).searchByName(eq(name), any(Pageable.class));
  }

  @Test
  void checkGender_MappingToEnum() {
    // This test indirectly tests the private checkGender method through the createProfile method

    // Arrange
    CreateProfileRequest malerequest = new CreateProfileRequest();
    malerequest.setProfileId("user1");
    malerequest.setFirstName("John");
    malerequest.setLastName("Doe");
    malerequest.setGender("Male");

    CreateProfileRequest femalerequest = new CreateProfileRequest();
    femalerequest.setProfileId("user2");
    femalerequest.setFirstName("Jane");
    femalerequest.setLastName("Doe");
    femalerequest.setGender("Female");

    CreateProfileRequest otherrequest = new CreateProfileRequest();
    otherrequest.setProfileId("user3");
    otherrequest.setFirstName("Alex");
    otherrequest.setLastName("Doe");
    otherrequest.setGender("Other");

    when(profileRepository.existsById(anyString())).thenReturn(false);
    when(profileRepository.save(any(Profile.class))).thenAnswer(invocation -> invocation.getArgument(0));

    // Act & Assert - Male
    ProfileResponse maleResult = profileService.createProfile(malerequest);
    assertEquals("Male", maleResult.getGender());

    // Act & Assert - Female
    ProfileResponse femaleResult = profileService.createProfile(femalerequest);
    assertEquals("Female", femaleResult.getGender());

    // Act & Assert - Other
    ProfileResponse otherResult = profileService.createProfile(otherrequest);
    assertEquals("Other", otherResult.getGender());
  }
}
