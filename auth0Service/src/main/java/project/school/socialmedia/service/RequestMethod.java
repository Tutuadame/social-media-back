package project.school.socialmedia.service;

public enum RequestMethod {
  GET, POST, PATCH, DELETE;

  @Override
  public String toString() {
    return this.name().toUpperCase();
  }
}
