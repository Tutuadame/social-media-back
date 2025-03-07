package project.school.socialmedia.dao;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
  @Column
  private long conversationId;
  @Column
  private String memberId;
  @Column
  private LocalDateTime sentAt;
  @Column
  private String content;

  public Message(long conversationId, String memberId, LocalDateTime sentAt, String content) {
    this.conversationId = conversationId;
    this.memberId = memberId;
    this.sentAt = sentAt;
    this.content = content;
  }


}
