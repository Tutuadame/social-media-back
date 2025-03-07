package project.school.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.school.socialmedia.dao.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
}
