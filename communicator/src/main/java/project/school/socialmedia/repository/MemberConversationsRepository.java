package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.domain.MemberConversations;

import java.util.List;

@Repository
public interface MemberConversationsRepository extends JpaRepository<MemberConversations, Long> {
  Page<MemberConversations> findByMember_Id(String memberId, Pageable pageable);

  List<MemberConversations> findByConversation_Id(Long conversationId);

  @Query("SELECT EXISTS (SELECT 1 FROM MemberConversations mc WHERE mc.conversation.id = :conversationId AND mc.member.id = :memberId)")
  boolean isMemberOfConversation(@Param("conversationId") Long conversationId, @Param("memberId") String memberId);

  void deleteByMember_Id(String id);

  void deleteByMember_IdAndConversation_Id(String memberId, Long conversationId);
}


