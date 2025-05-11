package project.school.socialmedia.social_profile.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.controller.PostController;
import project.school.socialmedia.dto.request.post.CreatePostRequest;
import project.school.socialmedia.dto.response.post.PostResponse;
import project.school.socialmedia.service.PostService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PostControllerTest {

  @Mock
  private PostService postService;

  @InjectMocks
  private PostController postController;

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
    testPostResponse.setLikes(5);
    testPostResponse.setDislikes(2);
  }

  @Test
  void getProfilePosts_ReturnsPageOfPosts() {
    // Arrange
    String profileId = "user123";
    int pageNumber = 0;
    int pageSize = 10;

    List<PostResponse> posts = Collections.singletonList(testPostResponse);
    Page<PostResponse> expectedPage = new PageImpl<>(posts);

    when(postService.getPostsFromUser(any(Pageable.class), eq(profileId))).thenReturn(expectedPage);

    // Act
    ResponseEntity<Page<PostResponse>> response = postController.getProfilePosts(profileId, pageNumber, pageSize);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<PostResponse> actualPage = response.getBody();
    assertNotNull(actualPage);
    assertEquals(1, actualPage.getContent().size());
    assertEquals(1L, actualPage.getContent().getFirst().getId());
    assertEquals("user123", actualPage.getContent().getFirst().getProfileId());
    assertEquals("Test post content", actualPage.getContent().getFirst().getContent());
    verify(postService).getPostsFromUser(any(Pageable.class), eq(profileId));
  }

  @Test
  void getConnectionPosts_ReturnsPageOfPosts() {
    // Arrange
    String profileId = "user123";
    int pageNumber = 0;
    int pageSize = 10;

    // Create a different post for connection
    PostResponse connectionPost = new PostResponse();
    connectionPost.setId(2L);
    connectionPost.setProfileId("user456");
    connectionPost.setContent("Connection post content");
    connectionPost.setCreatedAt(LocalDateTime.now());
    connectionPost.setLikes(3);
    connectionPost.setDislikes(1);

    List<PostResponse> posts = List.of(connectionPost);
    Page<PostResponse> expectedPage = new PageImpl<>(posts);

    when(postService.getPostsFromConnections(any(Pageable.class), eq(profileId))).thenReturn(expectedPage);

    // Act
    ResponseEntity<Page<PostResponse>> response = postController.getConnectionPosts(profileId, pageNumber, pageSize);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<PostResponse> actualPage = response.getBody();
    assertNotNull(actualPage);
    assertEquals(1, actualPage.getContent().size());
    assertEquals(2L, actualPage.getContent().getFirst().getId());
    assertEquals("user456", actualPage.getContent().getFirst().getProfileId());
    assertEquals("Connection post content", actualPage.getContent().getFirst().getContent());
    verify(postService).getPostsFromConnections(any(Pageable.class), eq(profileId));
  }

  @Test
  void createPost_ReturnsCreatedPost() {
    // Arrange
    CreatePostRequest request = new CreatePostRequest();
    request.setProfileId("user123");
    request.setContent("New post content");

    when(postService.createPost(request.getProfileId(), request.getContent())).thenReturn(testPostResponse);

    // Act
    ResponseEntity<PostResponse> response = postController.createPost(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    PostResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals(1L, actualResponse.getId());
    assertEquals("user123", actualResponse.getProfileId());
    assertEquals("Test post content", actualResponse.getContent());
    assertEquals(5, actualResponse.getLikes());
    assertEquals(2, actualResponse.getDislikes());
    verify(postService).createPost(request.getProfileId(), request.getContent());
  }

  @Test
  void deletePost_ReturnsNoContent() {
    // Arrange
    long postId = 1L;
    doNothing().when(postService).deletePost(postId);

    // Act
    ResponseEntity<String> response = postController.deletePost(postId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("Post deleted!", response.getBody());
    verify(postService).deletePost(postId);
  }
}
