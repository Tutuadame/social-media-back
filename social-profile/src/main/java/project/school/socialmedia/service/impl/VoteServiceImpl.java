package project.school.socialmedia.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import project.school.socialmedia.config.KafkaConfigProps;
import project.school.socialmedia.domain.Post;
import project.school.socialmedia.domain.Profile;
import project.school.socialmedia.domain.Vote;
import project.school.socialmedia.dto.kafka.Notification;
import project.school.socialmedia.dto.request.vote.CreateVoteRequest;
import project.school.socialmedia.dto.response.post.PostResponse;
import project.school.socialmedia.dto.response.vote.CheckVoteResponse;
import project.school.socialmedia.repository.PostRepository;
import project.school.socialmedia.repository.ProfileRepository;
import project.school.socialmedia.repository.VoteRepository;
import project.school.socialmedia.service.VoteService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class VoteServiceImpl implements VoteService {

  private final VoteRepository voteRepository;

  private final PostRepository postRepository;

  private final ProfileRepository profileRepository;

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final KafkaConfigProps kafkaConfigProps;

  private final ObjectMapper objectMapper;

  @Override
  public PostResponse addVote(CreateVoteRequest request) {
    Profile profile = profileRepository.findById(request.getProfileId())
            .orElseThrow(() -> new NoSuchElementException("Profile not found"));

    Post post = postRepository.findById(request.getPostId())
            .orElseThrow(() -> new NoSuchElementException("Post not found"));

    Vote vote = voteRepository.findByPostIdAndProfileId(request.getPostId(), request.getProfileId())
            .orElse(null);

    if (vote != null) {
      if (vote.isVote() == request.isVote()) {
        voteRepository.delete(vote);
      } else {
        vote.setVote(request.isVote());
        voteRepository.save(vote);
      }
    } else {
      vote = new Vote(request.isVote(), post, profile);
      voteRepository.save(vote);

      checkAndSendNotification(vote);
    }

    int likes = voteRepository.countByPostIdAndVote(post.getId(), true);
    int dislikes = voteRepository.countByPostIdAndVote(post.getId(), false);

    return new PostResponse(post, likes, dislikes);
  }

  @Override
  public CheckVoteResponse checkVote(String profileId, String postId) {
    return voteRepository.findByPostIdAndProfileId(Long.parseLong(postId), profileId)
            .map(vote -> new CheckVoteResponse(vote.isVote() ? "like" : "dislike"))
            .orElse(new CheckVoteResponse("none"));
  }

  private Notification createNotification(Post post, Vote vote, LocalDateTime sentAt) {
    String reaction = vote.isVote() ? "like" : "dislike";
    String notificationMessage = "You got a new " + reaction +" on one of your post";

    List<String> receiverIds = List.of(post.getProfile().getId());

    return Notification.builder()
            .createdAt(sentAt)
            .userIds(receiverIds)
            .message(notificationMessage)
            .build();
  }

  private void sendNotificationWithKafka(Notification notification) throws JsonProcessingException {
    final String payload = objectMapper.writeValueAsString(notification);
    kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
  }

  private void checkAndSendNotification(Vote vote) {
    try {
      Post post = postRepository
              .findById(vote.getPost().getId())
              .orElseThrow(() -> new NoSuchElementException("Post not found"));

        Notification notification = createNotification(post, vote, LocalDateTime.now());
        sendNotificationWithKafka(notification);
    } catch (Exception e) {
      System.out.println("Failed to send vote notification: "+e.getMessage());
    }
  }

}
