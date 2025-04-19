package project.school.socialmedia.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.dto.request.vote.CreateVoteRequest;
import project.school.socialmedia.dto.response.post.PostResponse;
import project.school.socialmedia.dto.response.vote.CheckVoteResponse;
import project.school.socialmedia.service.VoteService;

@RestController
@AllArgsConstructor
@RequestMapping("/voteApi")
public class VoteController {

  private final VoteService voteService;

  @PostMapping("/vote")
  public ResponseEntity<PostResponse> addVoteToPost (
          @RequestBody CreateVoteRequest createVoteRequest
  ) {
    PostResponse voteResponse = voteService.addVote(createVoteRequest);
    return ResponseEntity.status(HttpStatus.CREATED).body(voteResponse);
  }

  @GetMapping("/vote/{profileId}/{postId}")
  public ResponseEntity<CheckVoteResponse> checkVote (
          @PathVariable String profileId,
          @PathVariable String postId
  ) {
    CheckVoteResponse voteResponse = voteService.checkVote(profileId, postId);
    return ResponseEntity.status(HttpStatus.OK).body(voteResponse);
  }
}
