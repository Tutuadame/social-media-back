package project.school.socialmedia.service;

public interface TokenService {

  String getManagementToken();

  boolean isTokenExpired(String token);

  boolean isTokenSet(String token);
}
