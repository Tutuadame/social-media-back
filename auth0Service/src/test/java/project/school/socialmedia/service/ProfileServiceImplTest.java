package project.school.socialmedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.service.impl.ProfileServiceImpl;
import project.school.socialmedia.util.RequestUtils;
import project.school.socialmedia.util.ServiceUtils;

import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ProfileServiceImplTest {

  @Mock
  private RequestUtils requestUtils;

  @Mock
  private ServiceUtils serviceUtils;

  @Mock
  private HttpResponse<String> httpResponse;

  @InjectMocks
  private ProfileServiceImpl profileService;

  private final String TEST_USER_ID = "user123";
  private final String TEST_ACCESS_TOKEN = "test.access.token";
  private final String TEST_DOMAIN = "test.auth0.com";
  private final String TEST_CLIENT_ID = "test-client-id";
  private final String TEST_CLIENT_SECRET = "test-client-secret";
  private final String TEST_AUDIENCE = "https://test.auth0.com/api/v2/";
  private final String TEST_RESPONSE_BODY = "{\"updated\":true}";
  private final String CONNECTION = "Username-Password-Authentication";

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Set the private fields using ReflectionTestUtils
    ReflectionTestUtils.setField(profileService, "domain", TEST_DOMAIN);
    ReflectionTestUtils.setField(profileService, "clientId", TEST_CLIENT_ID);
    ReflectionTestUtils.setField(profileService, "clientSecret", TEST_CLIENT_SECRET);
    ReflectionTestUtils.setField(profileService, "audience", TEST_AUDIENCE);

    when(httpResponse.body()).thenReturn(TEST_RESPONSE_BODY);
  }

  @Test
  void updateUser_Success() throws JsonProcessingException {
    // Arrange
    UpdateProfileRequest updateRequest = new UpdateProfileRequest("name", "John Doe");
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("connection", CONNECTION);
    requestMap.put("client_id", TEST_CLIENT_ID);
    requestMap.put("name", "John Doe");

    String expectedUrl = "https://" + TEST_DOMAIN + "/api/v2/users/auth0%7C" + TEST_USER_ID;
    String requestBody = "{\"connection\":\"Username-Password-Authentication\",\"client_id\":\"test-client-id\",\"name\":\"John Doe\"}";

    when(requestUtils.buildUpdateRequestBody(eq(CONNECTION), eq(TEST_CLIENT_ID), any(UpdateProfileRequest.class)))
            .thenReturn(requestMap);
    when(serviceUtils.convertToString(requestMap)).thenReturn(requestBody);
    when(requestUtils.sendAuthorizedRequest(eq(RequestMethod.PATCH), eq(expectedUrl), eq(requestBody), eq(TEST_ACCESS_TOKEN)))
            .thenReturn(httpResponse);

    // Act
    String result = profileService.updateUser(updateRequest, TEST_USER_ID, TEST_ACCESS_TOKEN);

    // Assert
    assertEquals(TEST_RESPONSE_BODY, result);
    verify(requestUtils).buildUpdateRequestBody(eq(CONNECTION), eq(TEST_CLIENT_ID), eq(updateRequest));
    verify(serviceUtils).convertToString(requestMap);
    verify(requestUtils).sendAuthorizedRequest(eq(RequestMethod.PATCH), eq(expectedUrl), eq(requestBody), eq(TEST_ACCESS_TOKEN));
  }

  @Test
  void deleteUser_Success() {
    // Arrange
    String expectedUrl = "https://" + TEST_DOMAIN + "/api/v2/users/auth0%7C" + TEST_USER_ID;

    when(requestUtils.sendAuthorizedRequest(eq(RequestMethod.DELETE), eq(expectedUrl), eq(""), eq(TEST_ACCESS_TOKEN)))
            .thenReturn(httpResponse);

    // Act
    String result = profileService.deleteUser(TEST_USER_ID, TEST_ACCESS_TOKEN);

    // Assert
    assertEquals(TEST_RESPONSE_BODY, result);
    verify(requestUtils).sendAuthorizedRequest(eq(RequestMethod.DELETE), eq(expectedUrl), eq(""), eq(TEST_ACCESS_TOKEN));
  }

  @Test
  void generateAccessURL_ReturnsCorrectURL() throws Exception {
    // Use reflection to access the private method
    java.lang.reflect.Method method = ProfileServiceImpl.class.getDeclaredMethod("generateAccessURL", String.class);
    method.setAccessible(true);

    // Act
    String result = (String) method.invoke(profileService, TEST_USER_ID);

    // Assert
    String expectedUrl = "https://" + TEST_DOMAIN + "/api/v2/users/auth0%7C" + TEST_USER_ID;
    assertEquals(expectedUrl, result);
  }
}