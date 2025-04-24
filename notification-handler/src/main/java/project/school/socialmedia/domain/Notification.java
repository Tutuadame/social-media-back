package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "NOTIFICATION")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Notification {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ElementCollection
  @CollectionTable(
          name = "notification_user_ids",
          joinColumns = @JoinColumn(name = "notification_id")
  )
  @Column(name = "user_id")
  private List<String> userIds;

  @Column
  private String message;

  @Column
  private LocalDateTime createdAt;
}