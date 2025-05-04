package project.school.socialmedia.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.service.ProfileService;
import project.school.socialmedia.service.RequestMethod;
import project.school.socialmedia.util.RequestUtils;
import project.school.socialmedia.util.ServiceUtils;

import java.net.http.HttpResponse;
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

  public String updateUser(UpdateProfileRequest updateProfileRequest, String userId, String accessToken) throws JsonProcessingException {
    String updateURL = generateAccessURL(userId);
    Map<String, Object> requestMap = requestUtils.buildUpdateRequestBody(CONNECTION, clientId, updateProfileRequest);
    String requestBody = serviceUtils.convertToString(requestMap);
    HttpResponse<String> response = requestUtils.sendAuthorizedRequest(RequestMethod.PATCH, updateURL, requestBody, accessToken);
    return response.body();
  }

  public String deleteUser(String userId, String accessToken) {
    String deleteURL = generateAccessURL(userId);
    HttpResponse<String> response = requestUtils.sendAuthorizedRequest(RequestMethod.DELETE, deleteURL, "", accessToken);
    return response.body();
  }

  private String generateAccessURL(String userId) {
    return String.format("https://%s/api/v2/users/auth0%s%s", domain, SEPARATOR, userId);
  }
}