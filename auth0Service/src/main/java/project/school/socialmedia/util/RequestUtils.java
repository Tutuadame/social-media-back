package project.school.socialmedia.util;

import org.springframework.stereotype.Component;
import project.school.socialmedia.dto.request.UpdateProfileRequest;
import project.school.socialmedia.service.RequestMethod;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class RequestUtils {

  private static final String JSON = "application/json";

  public String sendSimpleRequest(RequestMethod method, String url, String body) throws RequestException {
    try (HttpClient client = HttpClient.newHttpClient()) {
      HttpRequest request = createSimpleRequest(url, method.toString(), body);
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if(checkResponse(method, response)){
        throw new RequestException(response.body());
      }
      return response.body();
    } catch (IOException | InterruptedException e) {
      throw new RequestException("Error sending request: " + e.getMessage());
    }
  }

  public HttpResponse<String> sendAuthorizedRequest(RequestMethod method, String url, String body, String accessToken) throws RequestException {
    try (HttpClient client = HttpClient.newHttpClient()) {
      HttpRequest request = createAuthorizedRequest(url, method.toString(), body, accessToken);
      HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
      if(checkResponse(method, response)){
        throw new RequestException(response.body());
      }
      return response;
    } catch (IOException | InterruptedException e) {
      throw new RequestException("Error sending request: " + e.getMessage());
    }
  }

  public Map<String, Object> buildUpdateRequestBody(String connection, String clientId, UpdateProfileRequest updateProfileRequest) {
    Map<String, Object> requestMap = new HashMap<>();
    requestMap.put("connection", connection);
    requestMap.put("client_id", clientId);
    requestMap.put(updateProfileRequest.getKey(), updateProfileRequest.getValue());
    return requestMap;
  }

  private HttpRequest createSimpleRequest(String url, String method, String body) {
    return HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", JSON)
            .header("Accept", JSON)
            .method(method, HttpRequest.BodyPublishers.ofString(body))
            .build();
  }

  private HttpRequest createAuthorizedRequest(String url, String method, String body, String accessToken) {
    return HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", JSON)
            .header("accept", JSON)
            .header("authorization", "Bearer " + accessToken)
            .method(method, HttpRequest.BodyPublishers.ofString(body))
            .build();
  }

  private boolean checkResponse(RequestMethod method, HttpResponse<String> response){
    return ((method.equals(RequestMethod.PATCH) || method.equals(RequestMethod.GET)) && response.statusCode() != 200) || (method.equals(RequestMethod.DELETE) && response.statusCode() != 204);
  }
}
