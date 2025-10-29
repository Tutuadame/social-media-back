package project.school.socialmedia.social_profile.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import project.school.socialprofile.domain.Connection;
import project.school.socialprofile.domain.ConnectionStatusEnum;
import project.school.socialprofile.domain.Post;
import project.school.socialprofile.domain.Profile;
import project.school.socialprofile.dto.response.post.PostResponse;
import project.school.socialprofile.repository.ConnectionRepository;
import project.school.socialprofile.repository.PostRepository;
import project.school.socialprofile.repository.ProfileRepository;
import project.school.socialprofile.repository.VoteRepository;
import project.school.socialprofile.service.impl.PostServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PostServiceImplTest {

  @Mock
  private PostRepository postRepository;

  @Mock
  private ProfileRepository profileRepository;

  @Mock
  private VoteRepository voteRepository;

  @Mock
  private ConnectionRepository connectionRepository;

  @InjectMocks
  private PostServiceImpl postService;

  private Profile testProfile;
  private Post testPost;
  private final Pageable pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Setup test profile
    testProfile = new Profile("user123", "avatar.jpg", null, "John", "Doe", "Test introduction");

    // Setup test post
    testPost = new Post("Test post content", testProfile);
  }

  @Test
  void createPost_ValidRequest_ReturnsPostResponse() {
    // Arrange
    String profileId = "user123";
    String content = "Test post content";

    when(profileRepository.findById(profileId)).thenReturn(Optional.of(testProfile));
    when(postRepository.save(any(Post.class))).thenReturn(testPost);
    when(voteRepository.countByPostIdAndVote(any(), eq(true))).thenReturn(5); // 5 likes
    when(voteRepository.countByPostIdAndVote(any(), eq(false))).thenReturn(2); // 2 dislikes

    // Act
    PostResponse result = postService.createPost(profileId, content);

    // Assert
    assertNotNull(result);
    assertEquals("user123", result.getProfileId());
    assertEquals("Test post content", result.getContent());
    assertEquals(5, result.getLikes());
    assertEquals(2, result.getDislikes());

    verify(profileRepository).findById(profileId);
    verify(postRepository).save(any(Post.class));
    verify(voteRepository).countByPostIdAndVote(any(), eq(true));
    verify(voteRepository).countByPostIdAndVote(any(), eq(false));
  }

  @Test
  void createPost_ProfileNotFound_ThrowsException() {
    // Arrange
    String profileId = "nonexistent";
    String content = "Test post content";

    when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> postService.createPost(profileId, content));
    verify(profileRepository).findById(profileId);
    verify(postRepository, never()).save(any(Post.class));
  }

  @Test
  void deletePost_ExistingPost_DeletesPost() {
    // Arrange
    long postId = 1L;

    when(postRepository.findById(postId)).thenReturn(Optional.of(testPost));
    doNothing().when(postRepository).delete(testPost);

    // Act
    postService.deletePost(postId);

    // Assert
    verify(postRepository).findById(postId);
    verify(postRepository).delete(testPost);
  }

  @Test
  void deletePost_NonExistingPost_ThrowsException() {
    // Arrange
    long postId = 999L;

    when(postRepository.findById(postId)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> postService.deletePost(postId));
    verify(postRepository).findById(postId);
    verify(postRepository, never()).delete(any(Post.class));
  }

  @Test
  void getPostsFromConnections_ReturnsConnectionPosts() {
    // Arrange
    String profileId = "user123";

    // Create connection profiles
    Profile connection1 = new Profile("user456", "avatar2.jpg", null, "Jane", "Smith", "Connection 1 intro");
    Profile connection2 = new Profile("user789", "avatar3.jpg", null, "Bob", "Johnson", "Connection 2 intro");

    // Create connections - one where user is initiator, one where user is target
    Connection conn1 = new Connection(1L, testProfile, connection1, ConnectionStatusEnum.ACCEPTED);
    Connection conn2 = new Connection(2L, connection2, testProfile, ConnectionStatusEnum.ACCEPTED);
    List<Connection> connections = Arrays.asList(conn1, conn2);

    // List of expected connection profile IDs that would be extracted
    List<String> expectedTargetIds = Arrays.asList("user456", "user789");

    // Create posts from connections
    Post post1 = new Post("Post from connection 1", connection1);
    Post post2 = new Post("Post from connection 2", connection2);
    List<Post> posts = Arrays.asList(post1, post2);
    Page<Post> postPage = new PageImpl<>(posts);

    when(connectionRepository.findByUserIdAndStatus(eq(profileId), eq(ConnectionStatusEnum.ACCEPTED)))
            .thenReturn(connections);
    when(postRepository.findPostsByConnections(eq(expectedTargetIds), any(Pageable.class)))
            .thenReturn(postPage);

    when(voteRepository.countByPostIdAndVote(any(), eq(true)))
            .thenReturn(3)
            .thenReturn(5);

    when(voteRepository.countByPostIdAndVote(any(), eq(false)))
            .thenReturn(1)
            .thenReturn(2);

    // Act
    Page<PostResponse> results = postService.getPostsFromConnections(pageable, profileId);

    // Assert
    assertNotNull(results);
    assertEquals(2, results.getContent().size());

    // Verify first post
    assertEquals("user456", results.getContent().getFirst().getProfileId());
    assertEquals("Post from connection 1", results.getContent().getFirst().getContent());
    assertEquals(3, results.getContent().getFirst().getLikes());
    assertEquals(1, results.getContent().getFirst().getDislikes());

    // Verify second post
    assertEquals("user789", results.getContent().get(1).getProfileId());
    assertEquals("Post from connection 2", results.getContent().get(1).getContent());
    assertEquals(5, results.getContent().get(1).getLikes());
    assertEquals(2, results.getContent().get(1).getDislikes());

    // Verify correct methods were called with correct parameters
    verify(connectionRepository).findByUserIdAndStatus(any(), eq(ConnectionStatusEnum.ACCEPTED));
    verify(postRepository).findPostsByConnections(eq(expectedTargetIds), any(Pageable.class));
    verify(voteRepository, times(2)).countByPostIdAndVote(any(),eq(true));
    verify(voteRepository, times(2)).countByPostIdAndVote(any(), eq(false));
  }

  @Test
  void getPostsFromConnections_NoConnections_ReturnsEmptyPage() {
    // Arrange
    String profileId = "user123";

    // Empty connections list
    when(connectionRepository.findByUserIdAndStatus(profileId, ConnectionStatusEnum.ACCEPTED))
            .thenReturn(Collections.emptyList());

    // Empty target IDs list resulting from stream operation
    List<String> emptyTargetIds = Collections.emptyList();

    // Empty page of posts
    Page<Post> emptyPage = new PageImpl<>(Collections.emptyList());
    when(postRepository.findPostsByConnections(eq(emptyTargetIds), any(Pageable.class)))
            .thenReturn(emptyPage);

    // Act
    Page<PostResponse> results = postService.getPostsFromConnections(pageable, profileId);

    // Assert
    assertNotNull(results);
    assertEquals(0, results.getContent().size());
    assertTrue(results.getContent().isEmpty());

    // Verify correct methods were called
    verify(connectionRepository).findByUserIdAndStatus(profileId, ConnectionStatusEnum.ACCEPTED);
    verify(postRepository).findPostsByConnections(eq(emptyTargetIds), any(Pageable.class));
    // No vote counts should be requested for empty page
    verify(voteRepository, never()).countByPostIdAndVote(anyLong(), anyBoolean());
  }

  @Test
  void getPostsFromUser_ReturnsUserPosts() {
    // Arrange
    String profileId = "user123";

    List<Post> posts = Collections.singletonList(testPost);
    Page<Post> postPage = new PageImpl<>(posts);

    when(postRepository.findPostByProfileId(eq(profileId), any(Pageable.class)))
            .thenReturn(postPage);
    when(voteRepository.countByPostIdAndVote(any(), eq(true))).thenReturn(5); // 5 likes
    when(voteRepository.countByPostIdAndVote(any(), eq(false))).thenReturn(2); // 2 dislikes

    // Act
    Page<PostResponse> results = postService.getPostsFromUser(pageable, profileId);

    // Assert
    assertNotNull(results);
    assertEquals(1, results.getContent().size());
    assertEquals("user123", results.getContent().getFirst().getProfileId());
    assertEquals("Test post content", results.getContent().getFirst().getContent());
    assertEquals(5, results.getContent().getFirst().getLikes());
    assertEquals(2, results.getContent().getFirst().getDislikes());

    verify(postRepository).findPostByProfileId(eq(profileId), any(Pageable.class));
    verify(voteRepository).countByPostIdAndVote(any(), eq(true));
    verify(voteRepository).countByPostIdAndVote(any(), eq(false));
  }

  @Test
  void getPostsFromUser_NoPosts_ReturnsEmptyPage() {
    // Arrange
    String profileId = "user123";

    when(postRepository.findPostByProfileId(eq(profileId), any(Pageable.class)))
            .thenReturn(new PageImpl<>(Collections.emptyList()));

    // Act
    Page<PostResponse> results = postService.getPostsFromUser(pageable, profileId);

    // Assert
    assertNotNull(results);
    assertEquals(0, results.getContent().size());
    assertTrue(results.getContent().isEmpty());

    verify(postRepository).findPostByProfileId(eq(profileId), any(Pageable.class));
  }
}