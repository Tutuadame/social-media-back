package project.school.socialmedia.domain;

import jakarta.persistence.*;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "Post")
@Data
@NoArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy= GenerationType.IDENTITY)
  private Long id;

  @Column
  private String content;

  @Column
  private LocalDateTime createdAt;

  @ManyToOne
  private Profile profile;

  public Post(String content, Profile profile){
    this.content = content;
    this.profile = profile;
    this.createdAt = LocalDateTime.now();
  }
}
