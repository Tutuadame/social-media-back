package project.school.socialprofile.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import project.school.socialprofile.config.KafkaConfigProps;
import project.school.socialprofile.domain.Post;
import project.school.socialprofile.domain.Profile;
import project.school.socialprofile.domain.Vote;
import project.school.socialprofile.dto.kafka.Notification;
import project.school.socialprofile.dto.request.vote.CreateVoteRequest;
import project.school.socialprofile.dto.response.post.PostResponse;
import project.school.socialprofile.dto.response.profile.BasicProfileResponse;
import project.school.socialprofile.dto.response.vote.CheckVoteResponse;
import project.school.socialprofile.repository.PostRepository;
import project.school.socialprofile.repository.ProfileRepository;
import project.school.socialprofile.repository.VoteRepository;
import project.school.socialprofile.service.VoteService;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

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

    BasicProfileResponse basicProfileResponses = new BasicProfileResponse(post.getProfile());

    return new PostResponse(post, basicProfileResponses, likes, dislikes);
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
