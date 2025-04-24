package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.domain.Connection;
import project.school.socialmedia.domain.ConnectionStatusEnum;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConnectionRepository extends JpaRepository<Connection, Long> {

  @Query("SELECT c FROM Connection c WHERE (c.initiator.id = :profileId OR c.target.id = :profileId) AND c.status = 'ACCEPTED'")
  List<Connection> findAcceptedByProfileId(@Param("profileId") String profileId);

  //This will return only the connections which came to profile, not sent by the profile!
  @Query("SELECT c FROM Connection c WHERE (c.target.id = :targetId) AND c.status = 'PENDING'")
  Page<Connection>findPendingByTargetId(@Param("targetId") String targetId, Pageable pageable);

  @Query("SELECT c FROM Connection c WHERE (c.initiator.id = :userId OR c.target.id = :userId) AND c.status = :status")
  List<Connection> findByUserIdAndStatus(@Param("userId") String userId, @Param("status") ConnectionStatusEnum status);

  @Query("""
  SELECT\s
    CASE\s
      WHEN c.initiator.id = :requesterId THEN c.target.id
      ELSE c.initiator.id
    END
  FROM Connection c
  WHERE c.status = 'ACCEPTED'
    AND ((c.initiator.id = :requesterId AND c.target.id IN :targetIds)
      OR (c.target.id = :requesterId AND c.initiator.id IN :targetIds))
  """)
  List<String> findConnectedIds(@Param("requesterId") String requesterId, @Param("targetIds") List<String> targetIds);

  @Query("""
  SELECT c FROM Connection c
  WHERE
    (c.initiator.id IN (:currentUserId, :targetUserId) AND c.target.id IN (:currentUserId, :targetUserId))     
  """)
  Optional<Connection> getConnectionStatus(@Param("currentUserId") String currentUserId, @Param("targetUserId") String targetUserId);


}
