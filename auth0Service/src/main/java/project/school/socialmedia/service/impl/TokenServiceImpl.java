package project.school.socialmedia.service.impl;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.school.socialmedia.dto.response.TokenResponse;
import project.school.socialmedia.service.RequestMethod;
import project.school.socialmedia.service.TokenService;
import project.school.socialmedia.exception.TokenServiceException;
import project.school.socialmedia.util.RequestException;
import project.school.socialmedia.util.RequestUtils;
import project.school.socialmedia.util.ServiceUtils;

import java.util.Date;
import java.util.Map;

@Service
public class TokenServiceImpl implements TokenService {

  private final RequestUtils requestUtils;
  private final ServiceUtils serviceUtils;
  @Value("${auth0.domain}")
  private String domain;
  @Value("${auth0.management.clientId}")
  private String clientId;
  @Value("${auth0.management.clientSecret}")
  private String clientSecret;
  @Value("${auth0.audience}")
  private String audience;

  @Autowired
  public TokenServiceImpl(RequestUtils requestUtils, ServiceUtils serviceUtils) {
    this.requestUtils = requestUtils;
    this.serviceUtils = serviceUtils;
  }

  public String getManagementToken() {
    String tokenURL = "https://" + domain + "/oauth/token";
    String[] keys = {"client_id", "client_secret", "audience", "grant_type"};
    String[] values = {clientId, clientSecret, audience, "client_credentials"};
    Map<String, Object> requestMap = serviceUtils.createMap(keys, values);

    try {
      String requestBody = serviceUtils.convertToString(requestMap);
      String response = requestUtils.sendSimpleRequest(RequestMethod.POST, tokenURL, requestBody);
      TokenResponse tokenResponse = serviceUtils.convertToTokenDTO(response);
      return tokenResponse.getAccessToken();
    } catch (RequestException | TokenServiceException | JsonProcessingException e) {
      return "Error: " + e.getMessage();
    }
  }

  public boolean isTokenExpired(String token) {
    try {
      DecodedJWT decodedJWT = JWT.decode(token);
      Date expiryDate = decodedJWT.getExpiresAt();
      return expiryDate == null || expiryDate.before(new Date());
    } catch (JWTDecodeException e) {
      throw new TokenServiceException("Invalid token format - " + e.getMessage());
    }
  }

  public boolean isTokenSet(String token){
    return token.isEmpty();
  }
}
