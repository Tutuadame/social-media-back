package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.dao.Conversation;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
  Optional<Conversation> findById(long id);
  @Override
  Page<Conversation> findAll(Pageable pageable);

  boolean existsById(long conversationId);
}
