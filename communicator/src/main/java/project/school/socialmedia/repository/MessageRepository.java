package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.domain.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
  Page<Message> findByConversationIdOrderBySentAtDesc(Long conversationId, Pageable pageable);
}
