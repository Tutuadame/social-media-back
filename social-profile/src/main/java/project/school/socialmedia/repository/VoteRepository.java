package project.school.socialmedia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import project.school.socialmedia.dao.Vote;

import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {

  @Query("SELECT COUNT(v) FROM Vote v WHERE v.postId = :postId AND v.vote = :vote")
  int countVotesByPostIdAndVote(@Param("postId") Long postId, @Param("vote") boolean vote);

  @Query("SELECT COUNT(v) > 0 FROM Vote v WHERE v.postId = :postId AND v.profileId = :profileId")
  boolean existsByPostIdAndProfileId(@Param("postId") Long postId, @Param("profileId") String profileId);

  @Query("SELECT v FROM Vote v WHERE v.postId = :postId AND v.profileId = :profileId")
  Optional<Vote> findByPostIdAndProfileId(@Param("postId") Long postId, @Param("profileId") String profileId);
}

