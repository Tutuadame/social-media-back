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
@ToString(exclude = {"memberConversations"})
public class Conversation {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column
  private String name;

  @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL)
  private List<MemberConversations> memberConversations;

  public Conversation(String name){
    this.name = name;
    this.memberConversations = new ArrayList<>();
  }
}
