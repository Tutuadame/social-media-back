package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.domain.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

  @Query("SELECT n FROM Notification n WHERE :userId MEMBER OF n.userIds")
  Page<Notification> findByUserId(Pageable pageable, String userId);

}