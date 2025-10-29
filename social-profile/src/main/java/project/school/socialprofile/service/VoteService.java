package project.school.socialprofile.service;


import project.school.socialprofile.dto.request.vote.CreateVoteRequest;
import project.school.socialprofile.dto.response.post.PostResponse;
import project.school.socialprofile.dto.response.vote.CheckVoteResponse;

public interface VoteService {
  PostResponse addVote(CreateVoteRequest createVoteRequest);
  CheckVoteResponse checkVote(String profileId, String postId);
}
