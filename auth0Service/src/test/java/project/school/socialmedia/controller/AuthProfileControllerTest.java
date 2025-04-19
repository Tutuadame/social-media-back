package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.dto.response.UserResponse;
import project.school.socialmedia.service.TokenServiceException;
import project.school.socialmedia.service.impl.ProfileServiceImpl;
import project.school.socialmedia.service.impl.TokenServiceImpl;
import project.school.socialmedia.util.RequestException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/*@SpringBootTest
class AuthProfileControllerTest {

  final String accessToken = "Test Token";
  final String testID = "auth0";

  @InjectMocks
  private AuthProfileController authProfileController;
  @Mock
  private ProfileServiceImpl profileServiceImpl;
  @Mock
  private TokenServiceImpl tokenServiceImpl;

  @BeforeEach
  void setUp() {
    this.authProfileController = new AuthProfileController(profileServiceImpl, tokenServiceImpl);
  }

  @AfterEach
  void reset(){
    this.authProfileController = null;
  }

  @Test
  @DisplayName("Should delete profile when user exists and ID is correct.")
  void shouldDeleteProfile() {
    // Arrange
    String responseMessage = "Profile was deleted!";
    when(profileServiceImpl.deleteUser(anyString(), anyString())).thenReturn(responseMessage);
    when(tokenServiceImpl.isTokenExpired(anyString())).thenReturn(true);
    when(tokenServiceImpl.getManagementToken()).thenReturn(accessToken);

    // Act
    ResponseEntity<String> response = authProfileController.deleteProfile(testID);

    // Assert
    assertEquals(responseMessage, response.getBody());

    // Verify
    verify(tokenServiceImpl, times(1)).getManagementToken();
    verify(tokenServiceImpl, times(1)).isTokenExpired(anyString());
    verify(profileServiceImpl, times(1)).deleteUser(testID, accessToken);
  }

  @Test
  @DisplayName("Should return error message when profile is not deleted.")
  void shouldReturnErrorWhenDeleteIsNotDone() {
    // Arrange
    String exceptionMessage = "Error: Bad request";
    when(profileServiceImpl.deleteUser(anyString(), anyString())).thenReturn(exceptionMessage);
    when(tokenServiceImpl.isTokenExpired(anyString())).thenReturn(true);
    when(tokenServiceImpl.getManagementToken()).thenReturn(accessToken);

    // Act
    ResponseEntity<String> response = authProfileController.deleteProfile(testID);

    // Assert
    assertEquals(exceptionMessage, response.getBody());

    // Verify
    verify(tokenServiceImpl, times(1)).getManagementToken();
    verify(tokenServiceImpl, times(1)).isTokenExpired(anyString());
    verify(profileServiceImpl, times(1)).deleteUser(testID, accessToken);
  }

  @Test
  @DisplayName("Delete endpoint Should return error message When it's not possible to receive a token.")
  void shouldIndicateWhenTokenIsWrongDuringDelete() {
    // Arrange
    String exceptionMessage = "Unable to retrieve management token";
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    TokenServiceException tokenServiceException = new TokenServiceException("test exception");
    when(tokenServiceImpl.isTokenExpired(anyString())).thenThrow(tokenServiceException);
    when(tokenServiceImpl.getManagementToken()).thenReturn(accessToken);

    // Act
    ResponseEntity<String> response = authProfileController.deleteProfile(testID);

    // Assert
    assertEquals(exceptionMessage, response.getBody());
    assertEquals(httpStatus, response.getStatusCode());

    // Verify
    verify(tokenServiceImpl, times(1)).isTokenExpired(anyString());
    verify(profileServiceImpl, times(0)).deleteUser(testID, accessToken);
  }

  @Test
  @DisplayName("Should update profile when user exists and ID is correct.")
  void shouldUpdateProfile() {
    // Arrange
    String responseMessage = "Update was successful!";
    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest("test", "test");
    when(profileServiceImpl.updateUser(any(UpdateProfileRequest.class), anyString(), anyString())).thenReturn(responseMessage);
    when(tokenServiceImpl.isTokenExpired(anyString())).thenReturn(true);
    when(tokenServiceImpl.isTokenSet(anyString())).thenReturn(false);
    when(tokenServiceImpl.getManagementToken()).thenReturn(accessToken);

    // Act
    ResponseEntity<String> response = authProfileController.updateProfile(updateProfileRequest, testID);

    // Assert
    assertEquals(responseMessage, response.getBody());

    // Verify
    verify(tokenServiceImpl, times(1)).isTokenSet("");
    verify(tokenServiceImpl, times(1)).getManagementToken();
    verify(tokenServiceImpl, times(1)).isTokenExpired(anyString());
    verify(profileServiceImpl, times(1)).updateUser(updateProfileRequest, testID, accessToken);
  }

  @Test
  @DisplayName("Should not update profile when user exists and ID is correct, but token is in invalid format.")
  void shouldNotUpdateProfileWhenTokenIsNotCorrect() {
    // Arrange
    String exceptionMessage = "Unable to retrieve management token";
    HttpStatus httpStatus = HttpStatus.UNAUTHORIZED;
    TokenServiceException tokenServiceException = new TokenServiceException("test exception");
    String token = "";
    UpdateProfileRequest updateProfileRequest = new UpdateProfileRequest("test", "test");

    when(profileServiceImpl.updateUser(any(UpdateProfileRequest.class), anyString(), anyString())).thenReturn(exceptionMessage);
    when(tokenServiceImpl.isTokenExpired(anyString())).thenThrow(tokenServiceException);
    when(tokenServiceImpl.getManagementToken()).thenReturn(accessToken);

    // Act
    ResponseEntity<String> response = authProfileController.updateProfile(updateProfileRequest, testID);

    // Assert
    assertEquals(exceptionMessage, response.getBody());
    assertEquals(httpStatus, response.getStatusCode());

    // Verify
    verify(tokenServiceImpl, times(1)).isTokenSet(token);
    verify(tokenServiceImpl, times(1)).isTokenExpired(anyString());
    verify(profileServiceImpl, times(0)).updateUser(updateProfileRequest, testID, token);
  }

  @Test
  @DisplayName("Should get profile When user exists and ID is correct.")
  void shouldGetProfile() throws JsonProcessingException {
    // Arrange
    UserResponse expectedUserResponse = getUserResponse();
    ResponseEntity<UserResponse> expectedResponse = ResponseEntity.status(200).body(expectedUserResponse);

    when(profileServiceImpl.getUser(anyString(), anyString())).thenReturn(expectedUserResponse);
    when(tokenServiceImpl.getManagementToken()).thenReturn(accessToken);
    when(tokenServiceImpl.isTokenExpired(anyString())).thenReturn(true);

    // Act
    ResponseEntity<?> response = authProfileController.getProfile(testID);

    // Assert
    assertEquals(expectedResponse, response);
    assertEquals(expectedResponse.getStatusCode(), response.getStatusCode());

    // Verify
    verify(tokenServiceImpl, times(1)).isTokenSet("");
    verify(tokenServiceImpl, times(1)).isTokenExpired(anyString());
    verify(profileServiceImpl, times(1)).getUser(testID, accessToken);
  }

  @Test
  @DisplayName("Should get profile When user exists and ID is correct.")
  void shouldGetErrorMessageWhenBadRequest() throws JsonProcessingException {
    // Arrange
    String exceptionMessage = "Error: Bad request";
    RequestException requestException = new RequestException(exceptionMessage);
    ResponseEntity<String> expectedResponse = ResponseEntity.status(400).body(exceptionMessage);

    when(profileServiceImpl.getUser(anyString(), anyString())).thenThrow(requestException);
    when(tokenServiceImpl.getManagementToken()).thenReturn(accessToken);
    when(tokenServiceImpl.isTokenExpired(anyString())).thenReturn(true);

    // Act
    ResponseEntity<?> response = authProfileController.getProfile(testID);

    // Assert
    assertEquals(expectedResponse, response);

    // Verify
    verify(tokenServiceImpl, times(1)).isTokenSet("");
    verify(tokenServiceImpl, times(1)).isTokenExpired(anyString());
    verify(profileServiceImpl, times(1)).getUser(testID, accessToken);
  }

  private UserResponse getUserResponse(){
    return new UserResponse("test", "test", false, "test", "test", "test", "test", "test", "test", 0, null);
  }
}
*/
