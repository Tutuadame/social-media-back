package project.school.socialmedia.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.dao.Member;
import project.school.socialmedia.dto.request.member.AddMemberRequest;
import project.school.socialmedia.dto.request.member.CreateMemberRequest;
import project.school.socialmedia.dto.request.member.DeleteMemberRequest;
import project.school.socialmedia.dto.request.member.GetMembersRequest;
import project.school.socialmedia.service.MemberService;

@RestController
@RequestMapping(value = "/memberApi")
@CrossOrigin(origins = "https://social.media:3000")
public class MemberController {

  private final MemberService memberService;

  @Autowired
  public MemberController(MemberService memberService) {
    this.memberService = memberService;
  }

  //Registration Flow
  //This should be string as well, since we don't need the actual the member
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
    return ResponseEntity.status(204).body(result);
  }

  //Chat Management Flow
  @PostMapping("/add")
  public ResponseEntity<Member> addMember(
          @RequestBody AddMemberRequest addMemberRequest
  ) {
    return ResponseEntity.status(201).body(
            memberService.add(addMemberRequest)
    );
  }

  //Chat Management Flow
  @DeleteMapping("/members")
  public ResponseEntity<String> deleteMemberFromConversation(
          @RequestBody DeleteMemberRequest deleteMemberRequest
  ) {
    String message = memberService.deleteFromConversation(
            deleteMemberRequest.getMemberId(), deleteMemberRequest.getConversationId()
    );
    return ResponseEntity.status(204).body(message);
  }

  //Chat Management Flow
  @PostMapping("/members/{conversationId}")
  public ResponseEntity<Page<Member>> getMembers(
          @PathVariable long conversationId,
          @RequestBody GetMembersRequest getMembersRequest
  ) {
    Pageable pageable = PageRequest.of(getMembersRequest.getPageNumber(), getMembersRequest.getPageSize());
    Page<Member> memberPage = memberService.getConversationMembers(conversationId, pageable);
    return ResponseEntity.status(200).body(memberPage);
  }
}
