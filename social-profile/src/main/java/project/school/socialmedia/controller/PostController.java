package project.school.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.dto.request.post.CreatePostRequest;
import project.school.socialmedia.dto.request.post.GetPageablePostsRequest;
import project.school.socialmedia.dto.response.post.PostResponse;
import project.school.socialmedia.service.PostService;

@RestController
@AllArgsConstructor
@RequestMapping("/postApi")
public class PostController {

  private final PostService postService;

  @PostMapping("/activity/{profileId}")
  public ResponseEntity<Page<PostResponse>> getProfilePosts(
          @PathVariable String profileId,
          @RequestBody GetPageablePostsRequest getPageablePostsRequest
  ) {
    Pageable pageable = PageRequest.of(getPageablePostsRequest.getPageNumber(), getPageablePostsRequest.getPageSize());
    Page<PostResponse> postResponses = postService.getPostsFromUser(
            pageable, profileId
    );
    return ResponseEntity.status(HttpStatus.OK).body(postResponses);
  }

  @PostMapping("/home/{profileId}")
  public ResponseEntity<Page<PostResponse>> getConnectionPosts(
          @PathVariable String profileId,
          @RequestBody GetPageablePostsRequest getPageablePostsRequest
  ) {
    Pageable pageable = PageRequest.of(getPageablePostsRequest.getPageNumber(), getPageablePostsRequest.getPageSize());
    Page<PostResponse> postResponses = postService.getPostsFromConnections(
            pageable, profileId
    );
    return ResponseEntity.status(HttpStatus.OK).body(postResponses);
  }

  @PostMapping("/new")
  public ResponseEntity<PostResponse> createPost(
          @RequestBody CreatePostRequest createPostRequest
  ){
    PostResponse postResponse = postService.createPost(
            createPostRequest.getProfileId(),
            createPostRequest.getContent()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
  }

  @DeleteMapping("/{postId}")
  public ResponseEntity<String> deletePost(
          @PathVariable Long postId
  ) {
    postService.deletePost(postId);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Post deleted!");
  }
}
