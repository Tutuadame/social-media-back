package project.school.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.dao.Member;
import project.school.socialmedia.dto.request.member.AddMemberRequest;
import project.school.socialmedia.dto.request.member.CreateMemberRequest;
import project.school.socialmedia.dto.request.member.DeleteMemberFromConversationRequest;
import project.school.socialmedia.dto.response.member.MemberResponse;
import project.school.socialmedia.service.MemberService;

import java.util.List;

@RestController
@RequestMapping("/memberApi")
@AllArgsConstructor
public class MemberController {

  private final MemberService memberService;

  //Registration Flow
  @PostMapping("/new")
  public ResponseEntity<Member> createMember(
          @RequestBody CreateMemberRequest createMemberRequest
  ) {
    return ResponseEntity.status(201).body(
            memberService.create(createMemberRequest)
    );
  }

  //Delete Flow
  @DeleteMapping("/{memberId}")
  public ResponseEntity<String> deleteMember(
          @PathVariable String memberId
  ) {
    String result = memberService.delete(memberId);
    return ResponseEntity.status(HttpStatus.OK).body(result);
  }

  //Chat Management Flow
  @PostMapping("/add")
  public ResponseEntity<MemberResponse> addMember(
          @RequestBody AddMemberRequest addMemberRequest
  ) {
    return ResponseEntity.status(HttpStatus.CREATED).body(
            memberService.add(addMemberRequest)
    );
  }

  //Chat Management Flow
  @DeleteMapping("/members")
  public ResponseEntity<String> deleteMemberFromConversation(
          @RequestBody DeleteMemberFromConversationRequest deleteMemberFromConversationRequest
  ) {
    String message = memberService.deleteFromConversation(
            deleteMemberFromConversationRequest.getMemberId(), deleteMemberFromConversationRequest.getConversationId()
    );
    return ResponseEntity.status(HttpStatus.OK).body(message);
  }

  //Chat Management Flow
  @GetMapping("/search")
  public ResponseEntity<Page<MemberResponse>> searchForMembers(
    @RequestParam String name,
    @RequestParam String requesterId,
    @RequestParam int pageSize,
    @RequestParam int pageNumber
  ) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<MemberResponse> memberResponsePage = memberService.searchForMembersByName(
            name,
            requesterId,
            pageable
    );
    return ResponseEntity.status(HttpStatus.OK).body(memberResponsePage);
  }

  //Chat Management Flow
  @PostMapping("/members")
  public ResponseEntity<List<MemberResponse>> getMembers(
          @RequestBody List<String> members
  ) {
    List<MemberResponse> memberResponses = memberService.getConversationMembers(members);
    return ResponseEntity.status(HttpStatus.OK).body(memberResponses);
  }
}
