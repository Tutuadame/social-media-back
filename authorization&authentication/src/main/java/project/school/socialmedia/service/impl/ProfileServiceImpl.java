package project.school.socialmedia.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.dto.response.UserResponse;
import project.school.socialmedia.service.ProfileService;
import project.school.socialmedia.service.RequestMethod;
import project.school.socialmedia.util.RequestException;
import project.school.socialmedia.util.RequestUtils;
import project.school.socialmedia.util.ServiceUtils;

import java.util.Map;

@Service
public class ProfileServiceImpl implements ProfileService {

  private static final String SEPARATOR = "%7C";
  private static final String CONNECTION = "Username-Password-Authentication";
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
  public ProfileServiceImpl(RequestUtils requestUtils, ServiceUtils serviceUtils) {
    this.requestUtils = requestUtils;
    this.serviceUtils = serviceUtils;
  }

  public String updateUser(UpdateProfileRequest updateProfileRequest, String userId, String accessToken) {
    try {
      String updateURL = generateAccessURL(userId);
      Map<String, Object> requestMap = requestUtils.buildUpdateRequestBody(CONNECTION, clientId, updateProfileRequest);
      String requestBody = serviceUtils.convertToString(requestMap);
      return requestUtils.sendAuthorizedRequest(
              RequestMethod.PATCH, updateURL, requestBody, accessToken
      );

    } catch (RequestException | JsonProcessingException e) {
      return getErrorMessage(e);
    }
  }

  public UserResponse getUser(String userId, String accessToken) throws RequestException, JsonProcessingException {
    String getURL = "https://" + domain + "/api/v2/users/auth0" + SEPARATOR + userId;
    String response = requestUtils.sendAuthorizedRequest(RequestMethod.GET, getURL, "", accessToken);
    return serviceUtils.convertToUserResponse(response);
  }

  public String deleteUser(String userId, String accessToken) {
    String deleteURL = generateAccessURL(userId);
    try {
      return requestUtils.sendAuthorizedRequest(RequestMethod.DELETE, deleteURL, "", accessToken);
    } catch (RequestException e) {
      return getErrorMessage(e);
    }
  }

  private String generateAccessURL(String userId) {
    return String.format("https://%s/api/v2/users/auth0%s%s", domain, SEPARATOR, userId);
  }

  private String getErrorMessage(Exception e) {
    return "Error: " + e.getMessage();
  }
}