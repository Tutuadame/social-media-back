package project.school.socialmedia.social_profile.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.controller.ProfileController;
import project.school.socialmedia.dto.request.profile.CreateProfileRequest;
import project.school.socialmedia.dto.request.profile.UpdateIntroductionRequest;
import project.school.socialmedia.dto.response.profile.GenericProfileResponse;
import project.school.socialmedia.dto.response.profile.ProfileResponse;
import project.school.socialmedia.dto.response.profile.UpdateIntroductionResponse;
import project.school.socialmedia.service.ProfileService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProfileControllerTest {

  @Mock
  private ProfileService profileService;

  @InjectMocks
  private ProfileController profileController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void createProfile_ReturnsCreatedProfile() {
    // Arrange
    CreateProfileRequest request = new CreateProfileRequest();
    request.setProfileId("user123");
    request.setFirstName("John");
    request.setLastName("Doe");
    request.setGender("Male");

    ProfileResponse expectedResponse = new ProfileResponse(
            "user123",
            "Say something about yourself!",
            "MALE",
            "John",
            "Doe",
            "https://avatar.iran.liara.run/username?username=John+Doe"
    );

    when(profileService.createProfile(request)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<ProfileResponse> response = profileController.createProfile(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    ProfileResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals("user123", actualResponse.getId());
    assertEquals("John", actualResponse.getFirstName());
    assertEquals("Doe", actualResponse.getLastName());
    assertEquals("MALE", actualResponse.getGender());
    verify(profileService).createProfile(request);
  }

  @Test
  void deleteProfile_ReturnsNoContent() {
    // Arrange
    String profileId = "user123";

    // Act
    ResponseEntity<String> response = profileController.deleteProfile(profileId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("Profile deleted!", response.getBody());
    verify(profileService).deleteProfile(profileId);
  }

  @Test
  void getProfile_ReturnsProfileResponse() {
    // Arrange
    String profileId = "user123";
    ProfileResponse expectedResponse = new ProfileResponse(
            "user123",
            "This is my introduction",
            "MALE",
            "John",
            "Doe",
            "https://avatar.iran.liara.run/username?username=John+Doe"
    );

    when(profileService.getUserProfile(profileId)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<ProfileResponse> response = profileController.getProfile(profileId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    ProfileResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals("user123", actualResponse.getId());
    assertEquals("This is my introduction", actualResponse.getIntroduction());
    assertEquals("John", actualResponse.getFirstName());
    assertEquals("Doe", actualResponse.getLastName());
    assertEquals("MALE", actualResponse.getGender());
    verify(profileService).getUserProfile(profileId);
  }

  @Test
  void updateIntroduction_ReturnsUpdatedResponse() {
    // Arrange
    String profileId = "user123";
    UpdateIntroductionRequest request = new UpdateIntroductionRequest();
    request.setIntroduction("My new introduction text");

    UpdateIntroductionResponse expectedResponse = new UpdateIntroductionResponse("user123", "My new introduction text");

    when(profileService.updateIntroduction(profileId, request.getIntroduction())).thenReturn(expectedResponse);

    // Act
    ResponseEntity<UpdateIntroductionResponse> response =
            profileController.updateIntroduction(profileId, request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    UpdateIntroductionResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals("user123", actualResponse.getProfileId());
    assertEquals("My new introduction text", actualResponse.getIntroduction());
    verify(profileService).updateIntroduction(profileId, request.getIntroduction());
  }

  @Test
  void searchProfileByName_ReturnsProfilesPage() {
    // Arrange
    String name = "John";
    int pageNumber = 0;
    int pageSize = 10;

    List<GenericProfileResponse> profiles = Arrays.asList(
            new GenericProfileResponse("user123", "John", "Doe", "picture1.jpg"),
            new GenericProfileResponse("user456", "John", "Smith", "picture2.jpg")
    );
    Page<GenericProfileResponse> expectedPage = new PageImpl<>(profiles);

    when(profileService.searchByName(eq(name), any(Pageable.class))).thenReturn(expectedPage);

    // Act
    ResponseEntity<Page<GenericProfileResponse>> response =
            profileController.searchProfileByName(name, pageNumber, pageSize);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<GenericProfileResponse> actualPage = response.getBody();
    assertNotNull(actualPage);
    assertEquals(2, actualPage.getContent().size());
    assertEquals("user123", actualPage.getContent().getFirst().getId());
    assertEquals("John", actualPage.getContent().get(0).getFirstName());
    assertEquals("Doe", actualPage.getContent().get(0).getLastName());
    assertEquals("user456", actualPage.getContent().get(1).getId());
    verify(profileService).searchByName(eq(name), any(Pageable.class));
  }
}