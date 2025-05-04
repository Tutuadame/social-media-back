package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

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

  @ManyToOne
  @JoinColumn(name = "post_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Post post;

  @ManyToOne
  @JoinColumn(name = "profile_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Profile profile;

  public Vote(boolean vote, Post post, Profile profile) {
    this.vote = vote;
    this.post = post;
    this.profile = profile;
  }
}


