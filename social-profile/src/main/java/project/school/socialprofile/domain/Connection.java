package project.school.socialprofile.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "CONNECTIONS",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"initiator_id", "target_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Connection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "initiator_id")
  @OnDelete(action = OnDeleteAction.CASCADE)  // Add this
  private Profile initiator;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "target_id")
  @OnDelete(action = OnDeleteAction.CASCADE)  // Add this
  private Profile target;

  @Column(nullable = false)
  @Enumerated(EnumType.STRING)
  private ConnectionStatusEnum status;

  public Connection(Profile initiator, Profile target, ConnectionStatusEnum status) {
    this.initiator = initiator;
    this.target = target;
    this.status = status;
  }
}