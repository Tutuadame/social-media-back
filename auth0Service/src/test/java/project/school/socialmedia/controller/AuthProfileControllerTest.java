package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.exception.TokenServiceException;
import project.school.socialmedia.service.impl.ProfileServiceImpl;
import project.school.socialmedia.service.impl.TokenServiceImpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class AuthProfileControllerTest {

  @Mock
  private ProfileServiceImpl profileService;

  @Mock
  private TokenServiceImpl tokenService;

  @InjectMocks
  private AuthProfileController authProfileController;

  private MockMvc mockMvc;
  private ObjectMapper objectMapper;
  private final String TEST_USER_ID = "user123";
  private final String TEST_TOKEN = "valid.test.token";
  private final String TEST_RESPONSE = "Profile updated successfully";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(authProfileController).build();
    objectMapper = new ObjectMapper();
  }

  @Test
  void updateProfile_Success() throws JsonProcessingException {
    // Arrange
    UpdateProfileRequest request = new UpdateProfileRequest("name", "John Doe");

    when(tokenService.isTokenSet(anyString())).thenReturn(true);
    when(tokenService.getManagementToken()).thenReturn(TEST_TOKEN);
    when(profileService.updateUser(any(UpdateProfileRequest.class), anyString(), anyString()))
            .thenReturn(TEST_RESPONSE);

    // Act
    ResponseEntity<String> response = authProfileController.updateProfile(request, TEST_USER_ID);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(TEST_RESPONSE, response.getBody());
    verify(profileService).updateUser(request, TEST_USER_ID, TEST_TOKEN);
  }

  @Test
  void updateProfile_TokenExpired_Success() throws JsonProcessingException {
    // Arrange
    UpdateProfileRequest request = new UpdateProfileRequest("name", "John Doe");

    when(tokenService.isTokenSet(anyString())).thenReturn(false);
    when(tokenService.isTokenExpired(anyString())).thenReturn(true);
    when(tokenService.getManagementToken()).thenReturn(TEST_TOKEN);
    when(profileService.updateUser(any(UpdateProfileRequest.class), anyString(), anyString()))
            .thenReturn(TEST_RESPONSE);

    // Act
    ResponseEntity<String> response = authProfileController.updateProfile(request, TEST_USER_ID);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals(TEST_RESPONSE, response.getBody());
    verify(tokenService).getManagementToken();
    verify(profileService).updateUser(request, TEST_USER_ID, TEST_TOKEN);
  }

  @Test
  void updateProfile_TokenServiceException() {
    // Arrange
    UpdateProfileRequest request = new UpdateProfileRequest("name", "John Doe");

    when(tokenService.isTokenSet(anyString())).thenReturn(true);
    when(tokenService.getManagementToken()).thenThrow(new TokenServiceException("Token error"));

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      authProfileController.updateProfile(request, TEST_USER_ID);
    });

    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    assertTrue(exception.getReason().contains("Unable to retrieve management token"));
  }

  @Test
  void deleteProfile_Success() {
    // Arrange
    when(tokenService.isTokenSet(anyString())).thenReturn(true);
    when(tokenService.getManagementToken()).thenReturn(TEST_TOKEN);
    when(profileService.deleteUser(anyString(), anyString())).thenReturn("User deleted successfully");

    // Act
    ResponseEntity<String> response = authProfileController.deleteProfile(TEST_USER_ID);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("User deleted successfully", response.getBody());
    verify(profileService).deleteUser(TEST_USER_ID, TEST_TOKEN);
  }

  @Test
  void deleteProfile_TokenError() {
    // Arrange
    when(tokenService.isTokenSet(anyString())).thenReturn(true);
    when(tokenService.getManagementToken()).thenThrow(new TokenServiceException("Token error"));

    // Act & Assert
    ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
      authProfileController.deleteProfile(TEST_USER_ID);
    });

    assertEquals(HttpStatus.UNAUTHORIZED, exception.getStatusCode());
    assertTrue(exception.getReason().contains("Unable to retrieve management token"));
  }
}