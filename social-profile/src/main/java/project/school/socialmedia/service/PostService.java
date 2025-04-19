package project.school.socialmedia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import project.school.socialmedia.dto.response.post.PostResponse;

import java.util.List;

public interface PostService {
  PostResponse createPost(String profileId, String content);
  void deletePost(long postId);
  Page<PostResponse> getPostsFromConnections(Pageable pageable, String profileId);
  Page<PostResponse> getPostsFromUser(Pageable pageable, String profileId);
}