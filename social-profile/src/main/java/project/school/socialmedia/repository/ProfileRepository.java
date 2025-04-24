package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.domain.Profile;

@Repository
public interface ProfileRepository extends JpaRepository<Profile, String> {

  @Query("""
  SELECT p FROM Profile p
  WHERE LOWER(p.firstName) LIKE LOWER(CONCAT('%', :name, '%'))
     OR LOWER(p.lastName) LIKE LOWER(CONCAT('%', :name, '%'))
     OR LOWER(CONCAT(p.firstName, ' ', p.lastName)) LIKE LOWER(CONCAT('%', :name, '%'))
  """)
  Page<Profile> searchByName(@Param("name") String name, Pageable pageable);
}
