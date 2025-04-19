package project.school.socialmedia.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.dao.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

  @Query("SELECT p FROM Post p WHERE p.profile.id IN :targetIds ORDER BY p.createdAt DESC")
  Page<Post> findPostsByConnections(@Param("targetIds") List<String> targetIds, Pageable pageable);

  Page<Post> findPostByProfileId(String profileId, Pageable pageable);
}

