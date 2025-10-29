package project.school.socialprofile.controller;

import lombok.AllArgsConstructor;
import project.school.socialprofile.dto.request.post.CreatePostRequest;
import project.school.socialprofile.dto.response.post.PostResponse;
import project.school.socialprofile.service.PostService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/postApi")
public class PostController {

    private final PostService postService;

    @GetMapping("/activity")
    public ResponseEntity<Page<PostResponse>> getProfilePosts(
        @RequestParam(name = "profileId") String profileId,
        @RequestParam(name = "pageNumber") int pageNumber,
        @RequestParam(name = "pageSize") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostResponse> postResponses = postService.getPostsFromUser(
            pageable, profileId
        );
        return ResponseEntity.status(HttpStatus.OK).body(postResponses);
    }

    @GetMapping("/home")
    public ResponseEntity<Page<PostResponse>> getConnectionPosts(
        @RequestParam(name = "profileId") String profileId,
        @RequestParam(name = "pageNumber") int pageNumber,
        @RequestParam(name = "pageSize") int pageSize
    ) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<PostResponse> postResponses = postService.getPostsFromConnections(
            pageable, profileId
        );
        return ResponseEntity.status(HttpStatus.OK).body(postResponses);
    }

    @PostMapping("/new")
    public ResponseEntity<PostResponse> createPost(
        @RequestBody(required = true) CreatePostRequest createPostRequest
    ){
        PostResponse postResponse = postService.createPost(
            createPostRequest.getProfileId(),
            createPostRequest.getContent()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<String> deletePost(
        @PathVariable(name = "postId") Long postId
    ) {
        postService.deletePost(postId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Post deleted!");
    }
}
