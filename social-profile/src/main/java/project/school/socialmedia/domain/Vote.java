package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "VOTES")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vote {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private boolean vote;

  private long postId;

  private String profileId;

  public Vote(boolean vote, long postId, String profileId) {
    this.vote = vote;
    this.postId = postId;
    this.profileId = profileId;
  }
}


