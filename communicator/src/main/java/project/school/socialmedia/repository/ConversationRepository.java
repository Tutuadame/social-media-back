package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.domain.Conversation;

import java.util.Optional;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
  Optional<Conversation> findById(long id);

  @Override
  Page<Conversation> findAll(Pageable pageable);

  boolean existsById(long conversationId);

  @Query("""
    SELECT c FROM Conversation c
    JOIN c.memberConversations m
    WHERE m.member.id = :memberId
    AND LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
  """)
  Page<Conversation> searchByNameForMember(@Param("name") String name, @Param("memberId") String memberId, Pageable pageable);

}