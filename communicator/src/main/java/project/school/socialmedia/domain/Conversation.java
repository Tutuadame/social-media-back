package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conversation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"memberConversations", "messages"})
public class Conversation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String name;

  @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<MemberConversations> memberConversations;

  @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Message> messages;  // Add this

  public Conversation(String name){
    this.name = name;
    this.memberConversations = new ArrayList<>();
    this.messages = new ArrayList<>();
  }
}
