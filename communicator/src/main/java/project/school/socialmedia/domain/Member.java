package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "member")
@NoArgsConstructor
@AllArgsConstructor
@Data
@ToString(exclude = {"memberConversations", "messages"})
public class Member {
  @Id
  @Column(name = "id")
  private String id;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String picture;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberConversations> memberConversations;

  @OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages;  // Add this

  public Member (String id, String firstName, String lastName, String picture) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.picture = picture;
    this.memberConversations = new ArrayList<>();
    this.messages = new ArrayList<>();
  }
}
