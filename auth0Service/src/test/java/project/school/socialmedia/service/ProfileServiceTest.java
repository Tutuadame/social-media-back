package project.school.socialmedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.dto.response.UserResponse;
import project.school.socialmedia.service.impl.ProfileServiceImpl;
import project.school.socialmedia.util.RequestException;
import project.school.socialmedia.util.RequestUtils;
import project.school.socialmedia.util.ServiceUtils;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/*@SpringBootTest
class ProfileServiceTest {

  @Mock
  private RequestUtils requestUtils;
  @Mock
  private ServiceUtils serviceUtils;
  @InjectMocks
  private ProfileServiceImpl profileService;

  private final String userId = "testId";

  @BeforeEach
  void setup(){
    this.profileService = new ProfileServiceImpl(requestUtils, serviceUtils);
  }

  @AfterEach
  void reset(){
    this.profileService = null;
  }

  @Test
  @DisplayName("Should send update request and receive answer when all fields are correct.")
  void shouldSendUpdateRequestWhenEverythingIsCorrect() throws JsonProcessingException {
    //Arrange
    String testAccessToken = "test token";
    UpdateProfileRequest updateProfileRequest = getUpdateRequest();
    String expectedMessage = "Update was successful!";
    Map<String, Object> testBodyMap = new HashMap<>();

    when(requestUtils.buildUpdateRequestBody(anyString(), anyString(), any(UpdateProfileRequest.class))).thenReturn(testBodyMap);
    when(serviceUtils.convertToString(testBodyMap)).thenReturn("");
    when(requestUtils.sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString())).thenReturn(expectedMessage);

    //Act
    String message = profileService.updateUser(updateProfileRequest, userId, testAccessToken);

    //Assert
    assertEquals(expectedMessage, message);

    //Verify
    verify(requestUtils, times(1)).sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString());
  }

  @Test
  @DisplayName("Should not send update request when there is not token set.")
  void shouldNotSendUpdateRequestWhenThereIsNoToken() throws JsonProcessingException {
    //Arrange
    String testAccessToken = "";
    UpdateProfileRequest updateProfileRequest = getUpdateRequest();
    Map<String, Object> testBodyMap = new HashMap<>();
    RequestException requestException = new RequestException("Test exception");
    String expectedMessage = "Error: "+requestException.getMessage();

    when(requestUtils.buildUpdateRequestBody(anyString(), anyString(), any(UpdateProfileRequest.class))).thenReturn(testBodyMap);
    when(serviceUtils.convertToString(testBodyMap)).thenReturn("");
    when(requestUtils.sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString())).thenThrow(requestException);

    //Act
    String message = profileService.updateUser(updateProfileRequest, userId, testAccessToken);

    //Assert
    assertEquals(expectedMessage, message);

    //Verify
    verify(requestUtils, times(1)).sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString());
  }

  @Test
  @DisplayName("Should send get request to retrieve user when everything is correct.")
  void shouldSendGetRequestToRetrieveUser() throws JsonProcessingException {
    //Arrange
    String testAccessToken = "";

    when(serviceUtils.convertToUserResponse(anyString())).thenReturn(getUserResponse());
    when(requestUtils.sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString())).thenReturn("");

    //Act
    UserResponse response = profileService.getUser(userId, testAccessToken);

    //Assert
    assertEquals(getUserResponse(), response);

    //Verify
    verify(requestUtils, times(1)).sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString());
    verify(serviceUtils, times(1)).convertToUserResponse(anyString());
  }

  @Test
  @DisplayName("Should send delete request to delete user.")
  void shouldSendDeleteRequest() {
    //Arrange
    String testAccessToken = "";
    String expectedMessage = "Test message";

    when(requestUtils.sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString())).thenReturn(expectedMessage);

    //Act
    String message = profileService.deleteUser(userId, testAccessToken);

    //Assert
    assertEquals(expectedMessage, message);

    //Verify
    verify(requestUtils, times(1)).sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString());
  }

  @Test
  @DisplayName("Should throw error when delete request missing token.")
  void shouldThrowErrorWithoutTokenUsingDelete() {
    //Arrange
    String testAccessToken = "";
    String expectedMessage = "Error: Test exception";
    RequestException requestException = new RequestException("Test exception");

    when(requestUtils.sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString())).thenThrow(requestException);

    //Act
    String message = profileService.deleteUser(userId, testAccessToken);

    //Assert
    assertEquals(expectedMessage, message);

    //Verify
    verify(requestUtils, times(1)).sendAuthorizedRequest(any(RequestMethod.class), anyString(), anyString(), anyString());
  }

  private UpdateProfileRequest getUpdateRequest(){
    return new UpdateProfileRequest("test", "test");
  }

  private UserResponse getUserResponse(){
    return new UserResponse("test", "test", false, "test", "test", "test", "test", "test", "test", 0, null);
  }
}
*/