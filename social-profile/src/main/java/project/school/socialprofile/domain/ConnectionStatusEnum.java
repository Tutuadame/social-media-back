package project.school.socialprofile.domain;

public enum ConnectionStatusEnum {
  PENDING("PENDING"),
  ACCEPTED("ACCEPTED"),
  BLOCKED("BLOCKED");

  private final String displayName;

  ConnectionStatusEnum(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}

