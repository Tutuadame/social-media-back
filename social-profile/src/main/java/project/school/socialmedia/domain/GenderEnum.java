package project.school.socialmedia.domain;

import lombok.Getter;

@Getter
public enum GenderEnum {
  MALE("Male"),
  FEMALE("Female"),
  OTHER("Other");

  private final String displayName;

  GenderEnum(String displayName) {
    this.displayName = displayName;
  }

  @Override
  public String toString() {
    return displayName;
  }
}

