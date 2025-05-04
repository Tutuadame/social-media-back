package project.school.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.domain.Vote;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

  int countByPostIdAndVote(@Param("postId") Long postId, @Param("vote") boolean vote);

  boolean existsByPostIdAndProfileId(Long postId, String profileId);

  Optional<Vote> findByPostIdAndProfileId(Long postId, String profileId);
}

