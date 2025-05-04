package project.school.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import project.school.socialmedia.domain.Connection;
import project.school.socialmedia.domain.ConnectionStatusEnum;
import project.school.socialmedia.domain.Post;
import project.school.socialmedia.domain.Profile;
import project.school.socialmedia.dto.response.post.PostResponse;
import project.school.socialmedia.repository.ConnectionRepository;
import project.school.socialmedia.repository.PostRepository;
import project.school.socialmedia.repository.ProfileRepository;
import project.school.socialmedia.repository.VoteRepository;
import project.school.socialmedia.service.PostService;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class PostServiceImpl implements PostService {

  private final PostRepository postRepository;
  private final ProfileRepository profileRepository;
  private final VoteRepository voteRepository;
  private final ConnectionRepository connectionRepository;

  @Override
  public PostResponse createPost(String profileId, String content) {
    Profile profile = profileRepository.findById(profileId).orElseThrow(NoSuchElementException::new);
    Post post = new Post(content, profile);
    post = postRepository.save(post);
    int likes = voteRepository.countByPostIdAndVote(post.getId(), true);
    int dislikes = voteRepository.countByPostIdAndVote(post.getId(), false);
    return new PostResponse(post, likes, dislikes);
  }

  @Override
  public void deletePost(long postId) {
    Post post = postRepository.findById(postId).orElseThrow(NoSuchElementException::new);
    postRepository.delete(post);
  }

  @Override
  public Page<PostResponse> getPostsFromConnections(Pageable pageable, String profileId) {
    List<Connection> connections = connectionRepository.findByUserIdAndStatus(profileId, ConnectionStatusEnum.ACCEPTED);
    List<String> targetIds = connections.stream()
            .map(connection -> connection.getTarget().getId().equals(profileId) ?
            connection.getInitiator().getId() : connection.getTarget().getId())
            .toList();

    return postRepository.findPostsByConnections(targetIds, pageable).map((post) -> {
      int likes = voteRepository.countByPostIdAndVote(post.getId(), true);
      int dislikes = voteRepository.countByPostIdAndVote(post.getId(), false);
      return new PostResponse(post, likes, dislikes);
    });
  }

  @Override
  public Page<PostResponse> getPostsFromUser(Pageable pageable, String profileId) {
    Page<Post> posts = postRepository.findPostByProfileId(profileId, pageable);
    return posts.map(post -> {
      int likes = voteRepository.countByPostIdAndVote(post.getId(), true);
      int dislikes = voteRepository.countByPostIdAndVote(post.getId(), false);
      return new PostResponse(post, likes, dislikes);
    });
  }
}
