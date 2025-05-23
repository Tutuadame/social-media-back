package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(
        name = "member_conversations",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"member_id", "conversation_id"})
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberConversations {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_id")
  @OnDelete(action = OnDeleteAction.CASCADE)  // Add this
  private Member member;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conversation_id")
  @OnDelete(action = OnDeleteAction.CASCADE)  // Add this
  private Conversation conversation;

  public MemberConversations(Member member, Conversation conversation) {
    this.member = member;
    this.conversation = conversation;
  }
}
