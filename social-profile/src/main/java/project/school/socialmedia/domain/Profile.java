package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "Profile")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

  @Id
  @Column
  private String id;

  @Column
  private String picture;

  @Column
  @Enumerated(EnumType.STRING)
  private GenderEnum gender;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String introduction;

  @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Post> posts;

  // Add these to handle incoming connections
  @OneToMany(mappedBy = "initiator", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Connection> initiatedConnections;

  @OneToMany(mappedBy = "target", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Connection> targetedConnections;

  @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Vote> votes;

  public Profile(String profileId, String picture, GenderEnum gender, String firstName, String lastName, String introduction) {
    this.id = profileId;
    this.picture = picture;
    this.gender = gender;
    this.firstName = firstName;
    this.lastName = lastName;
    this.introduction = introduction;
  }
}
