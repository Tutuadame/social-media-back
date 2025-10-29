package project.school.socialprofile.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.school.socialprofile.dto.response.post.PostResponse;

public interface PostService {
  PostResponse createPost(String profileId, String content);
  void deletePost(long postId);
  Page<PostResponse> getPostsFromConnections(Pageable pageable, String profileId);
  Page<PostResponse> getPostsFromUser(Pageable pageable, String profileId);
}