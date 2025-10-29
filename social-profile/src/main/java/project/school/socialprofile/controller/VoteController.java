package project.school.socialprofile.controller;


import lombok.AllArgsConstructor;
import project.school.socialprofile.dto.request.vote.CreateVoteRequest;
import project.school.socialprofile.dto.response.post.PostResponse;
import project.school.socialprofile.dto.response.vote.CheckVoteResponse;
import project.school.socialprofile.service.VoteService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/voteApi")
public class VoteController {

  private final VoteService voteService;

  @PostMapping("/vote")
  public ResponseEntity<PostResponse> addVoteToPost (
      @RequestBody(required = true) CreateVoteRequest createVoteRequest
  ) {
    PostResponse voteResponse = voteService.addVote(createVoteRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(voteResponse);
  }

  @GetMapping("/vote/{profileId}/{postId}")
  public ResponseEntity<CheckVoteResponse> checkVote (
      @PathVariable(name = "profileId") String profileId,
      @PathVariable(name = "postId") String postId
  ) {
    CheckVoteResponse voteResponse = voteService.checkVote(profileId, postId);
    return ResponseEntity.status(HttpStatus.OK).body(voteResponse);
  }
}
