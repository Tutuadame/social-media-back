package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.school.socialmedia.domain.Member;

public interface MemberRepository extends JpaRepository<Member, String> {

  @Query("SELECT m FROM Member m WHERE LOWER(CONCAT(m.firstName, ' ', m.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))")
  Page<Member> searchByName(@Param("name") String name, Pageable pageable);
}
