package project.school.socialmedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import project.school.socialmedia.dto.response.TokenResponse;
import project.school.socialmedia.service.impl.TokenServiceImpl;
import project.school.socialmedia.util.RequestException;
import project.school.socialmedia.util.RequestUtils;
import project.school.socialmedia.util.ServiceUtils;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/*@SpringBootTest
class TokenServiceTest {

  @Mock
  RequestUtils requestUtils;
  @Mock
  ServiceUtils serviceUtils;
  @InjectMocks
  TokenServiceImpl tokenService;

  @BeforeEach
  void setup(){
    this.tokenService = new TokenServiceImpl(requestUtils, serviceUtils);
  }

  @AfterEach
  void reset(){
    this.tokenService = null;
  }

  @Test
  @DisplayName("Should get management token When request is correct.")
  void shouldGetManagementToken() throws JsonProcessingException {
    //Arrange
    TokenResponse testDTOToken = new TokenResponse("","","",0);

    when(serviceUtils.createMap(any(String[].class), any(String[].class))).thenReturn(new HashMap<>());
    when(serviceUtils.convertToString(any())).thenReturn("");
    when(requestUtils.sendSimpleRequest(any(RequestMethod.class), any(), any())).thenReturn("");
    when(serviceUtils.convertToTokenDTO("")).thenReturn(testDTOToken);

    //Act
    String token = tokenService.getManagementToken();

    //Assert
    assertEquals(testDTOToken.getAccessToken(), token);

    //Verify
    verify(requestUtils, times(1)).sendSimpleRequest(any(RequestMethod.class), any(), any());
  }

  @Test
  @DisplayName("Should get exception message When request is not wrong")
  void shouldGetErrorWhenRequestIsNotSuccessfulInsteadToken() throws JsonProcessingException {
    //Arrange
    RequestException requestException = new RequestException("Test exception");
    String expectedMessage = "Error: "+requestException.getMessage();

    when(serviceUtils.createMap(any(String[].class), any(String[].class))).thenReturn(new HashMap<>());
    when(serviceUtils.convertToString(any())).thenReturn("");
    when(requestUtils.sendSimpleRequest(any(RequestMethod.class), any(), any())).thenThrow(requestException);
    when(serviceUtils.convertToTokenDTO("")).thenReturn(null);

    //Act
    String message = tokenService.getManagementToken();

    //Assert
    assertEquals(expectedMessage, message);

    //Verify
    verify(requestUtils, times(1)).sendSimpleRequest(any(RequestMethod.class), any(), any());
  }
}*/
