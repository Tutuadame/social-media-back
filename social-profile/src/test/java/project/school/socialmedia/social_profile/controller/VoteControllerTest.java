package project.school.socialmedia.social_profile.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import project.school.socialprofile.controller.VoteController;
import project.school.socialprofile.dto.request.vote.CreateVoteRequest;
import project.school.socialprofile.dto.response.post.PostResponse;
import project.school.socialprofile.dto.response.vote.CheckVoteResponse;
import project.school.socialprofile.service.VoteService;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VoteControllerTest {

  @Mock
  private VoteService voteService;

  @InjectMocks
  private VoteController voteController;

  private PostResponse testPostResponse;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Create a test post response
    testPostResponse = new PostResponse();
    testPostResponse.setId(1L);
    testPostResponse.setProfileId("user123");
    testPostResponse.setContent("Test post content");
    testPostResponse.setCreatedAt(LocalDateTime.now());
    testPostResponse.setLikes(6); // Incremented after vote
    testPostResponse.setDislikes(2);
  }

  @Test
  void addVoteToPost_ReturnsUpdatedPost() {
    // Arrange
    CreateVoteRequest request = new CreateVoteRequest();
    request.setProfileId("user456");
    request.setPostId(1L);
    request.setVote(true); // Like

    when(voteService.addVote(request)).thenReturn(testPostResponse);

    // Act
    ResponseEntity<PostResponse> response = voteController.addVoteToPost(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    PostResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals(1L, actualResponse.getId());
    assertEquals("user123", actualResponse.getProfileId());
    assertEquals("Test post content", actualResponse.getContent());
    assertEquals(6, actualResponse.getLikes()); // Incremented after vote
    assertEquals(2, actualResponse.getDislikes());
    verify(voteService).addVote(request);
  }

  @Test
  void checkVote_ReturnsVoteStatus_Like() {
    // Arrange
    String profileId = "user456";
    String postId = "1";
    CheckVoteResponse expectedResponse = new CheckVoteResponse("like");

    when(voteService.checkVote(profileId, postId)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<CheckVoteResponse> response = voteController.checkVote(profileId, postId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    CheckVoteResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals("like", actualResponse.getType());
    verify(voteService).checkVote(profileId, postId);
  }

  @Test
  void checkVote_ReturnsVoteStatus_Dislike() {
    // Arrange
    String profileId = "user789";
    String postId = "1";
    CheckVoteResponse expectedResponse = new CheckVoteResponse("dislike");

    when(voteService.checkVote(profileId, postId)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<CheckVoteResponse> response = voteController.checkVote(profileId, postId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    CheckVoteResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals("dislike", actualResponse.getType());
    verify(voteService).checkVote(profileId, postId);
  }

  @Test
  void checkVote_ReturnsVoteStatus_None() {
    // Arrange
    String profileId = "user101";
    String postId = "1";
    CheckVoteResponse expectedResponse = new CheckVoteResponse("none");

    when(voteService.checkVote(profileId, postId)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<CheckVoteResponse> response = voteController.checkVote(profileId, postId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    CheckVoteResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals("none", actualResponse.getType());
    verify(voteService).checkVote(profileId, postId);
  }
}
