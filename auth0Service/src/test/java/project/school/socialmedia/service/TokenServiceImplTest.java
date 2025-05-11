package project.school.socialmedia.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;
import project.school.socialmedia.dto.response.TokenResponse;
import project.school.socialmedia.exception.TokenServiceException;
import project.school.socialmedia.service.RequestMethod;
import project.school.socialmedia.util.RequestException;
import project.school.socialmedia.util.RequestUtils;
import project.school.socialmedia.util.ServiceUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TokenServiceImplTest {

  @Mock
  private RequestUtils requestUtils;

  @Mock
  private ServiceUtils serviceUtils;

  @InjectMocks
  private TokenServiceImpl tokenService;

  private final String TEST_DOMAIN = "test.auth0.com";
  private final String TEST_CLIENT_ID = "test-client-id";
  private final String TEST_CLIENT_SECRET = "test-client-secret";
  private final String TEST_AUDIENCE = "https://test.auth0.com/api/v2/";
  private final String TEST_TOKEN = "test.token.value";
  private final TokenResponse TEST_TOKEN_RESPONSE = new TokenResponse(TEST_TOKEN, "read:users", "Bearer", 86400);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Set the private fields using ReflectionTestUtils
    ReflectionTestUtils.setField(tokenService, "domain", TEST_DOMAIN);
    ReflectionTestUtils.setField(tokenService, "clientId", TEST_CLIENT_ID);
    ReflectionTestUtils.setField(tokenService, "clientSecret", TEST_CLIENT_SECRET);
    ReflectionTestUtils.setField(tokenService, "audience", TEST_AUDIENCE);
  }

  @Test
  void getManagementToken_Success() throws JsonProcessingException {
    // Arrange
    String tokenUrl = "https://" + TEST_DOMAIN + "/oauth/token";
    String[] keys = {"client_id", "client_secret", "audience", "grant_type"};
    String[] values = {TEST_CLIENT_ID, TEST_CLIENT_SECRET, TEST_AUDIENCE, "client_credentials"};
    Map<String, Object> requestMap = new HashMap<>();
    String requestBody = "{\"client_id\":\"test-client-id\",\"client_secret\":\"test-client-secret\",\"audience\":\"https://test.auth0.com/api/v2/\",\"grant_type\":\"client_credentials\"}";
    String responseJson = "{\"access_token\":\"test.token.value\",\"scope\":\"read:users\",\"token_type\":\"Bearer\",\"expires_in\":86400}";

    when(serviceUtils.createMap(eq(keys), eq(values))).thenReturn(requestMap);
    when(serviceUtils.convertToString(requestMap)).thenReturn(requestBody);
    when(requestUtils.sendSimpleRequest(eq(RequestMethod.POST), eq(tokenUrl), eq(requestBody))).thenReturn(responseJson);
    when(serviceUtils.convertToTokenDTO(responseJson)).thenReturn(TEST_TOKEN_RESPONSE);

    // Act
    String result = tokenService.getManagementToken();

    // Assert
    assertEquals(TEST_TOKEN, result);
    verify(serviceUtils).createMap(eq(keys), eq(values));
    verify(serviceUtils).convertToString(requestMap);
    verify(requestUtils).sendSimpleRequest(eq(RequestMethod.POST), eq(tokenUrl), eq(requestBody));
    verify(serviceUtils).convertToTokenDTO(responseJson);
  }

  @Test
  void getManagementToken_RequestException() throws JsonProcessingException {
    // Arrange
    Map<String, Object> requestMap = new HashMap<>();
    String requestBody = "{\"client_id\":\"test-client-id\",\"client_secret\":\"test-client-secret\",\"audience\":\"https://test.auth0.com/api/v2/\",\"grant_type\":\"client_credentials\"}";

    when(serviceUtils.createMap(any(), any())).thenReturn(requestMap);
    when(serviceUtils.convertToString(requestMap)).thenReturn(requestBody);
    when(requestUtils.sendSimpleRequest(any(), anyString(), anyString())).thenThrow(new RequestException("Network error"));

    // Act
    String result = tokenService.getManagementToken();

    // Assert
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("Network error"));
  }

  @Test
  void getManagementToken_JsonProcessingException() throws JsonProcessingException {
    // Arrange
    Map<String, Object> requestMap = new HashMap<>();

    when(serviceUtils.createMap(any(), any())).thenReturn(requestMap);
    when(serviceUtils.convertToString(requestMap)).thenThrow(new JsonProcessingException("JSON error") {});

    // Act
    String result = tokenService.getManagementToken();

    // Assert
    assertTrue(result.startsWith("Error:"));
    assertTrue(result.contains("JSON error"));
  }

  @Test
  void isTokenExpired_ValidNonExpiredToken() {
    // Arrange
    String token = createTestToken(new Date(System.currentTimeMillis() + 3600000)); // 1 hour in the future

    // Act
    boolean result = tokenService.isTokenExpired(token);

    // Assert
    assertFalse(result);
  }

  @Test
  void isTokenExpired_ValidExpiredToken() {
    // Arrange
    String token = createTestToken(new Date(System.currentTimeMillis() - 3600000)); // 1 hour in the past

    // Act
    boolean result = tokenService.isTokenExpired(token);

    // Assert
    assertTrue(result);
  }

  @Test
  void isTokenExpired_InvalidToken() {
    // Arrange
    String invalidToken = "invalid.token.format";

    // Act & Assert
    TokenServiceException exception = assertThrows(TokenServiceException.class, () -> {
      tokenService.isTokenExpired(invalidToken);
    });

    assertTrue(exception.getMessage().contains("Invalid token format"));
  }

  // Helper method to create a test JWT token
  private String createTestToken(Date expiryDate) {
    return JWT.create()
            .withIssuer("test-issuer")
            .withExpiresAt(expiryDate)
            .sign(Algorithm.HMAC256("secret"));
  }
}