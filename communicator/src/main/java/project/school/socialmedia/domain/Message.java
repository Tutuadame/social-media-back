package project.school.socialmedia.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;

@Entity
@Table(name = "message")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Message {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "conversation_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Conversation conversation;  // Changed from long conversationId

  @ManyToOne
  @JoinColumn(name = "member_id")
  @OnDelete(action = OnDeleteAction.CASCADE)
  private Member member;  // Changed from String memberId

  @Column
  private LocalDateTime sentAt;

  @Column
  private String content;

  public Message(Conversation conversation, Member member, LocalDateTime sentAt, String content) {
    this.conversation = conversation;
    this.member = member;
    this.sentAt = sentAt;
    this.content = content;
  }
}
