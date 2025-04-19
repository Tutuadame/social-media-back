package project.school.socialmedia.service;


import project.school.socialmedia.dto.request.vote.CreateVoteRequest;
import project.school.socialmedia.dto.response.post.PostResponse;
import project.school.socialmedia.dto.response.vote.CheckVoteResponse;

public interface VoteService {
  PostResponse addVote(CreateVoteRequest createVoteRequest);
  CheckVoteResponse checkVote(String profileId, String postId);
}
