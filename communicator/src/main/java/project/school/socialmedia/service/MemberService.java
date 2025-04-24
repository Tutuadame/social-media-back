package project.school.socialmedia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import project.school.socialmedia.domain.Member;
import project.school.socialmedia.dto.request.member.AddMemberRequest;
import project.school.socialmedia.dto.request.member.CreateMemberRequest;
import project.school.socialmedia.dto.response.member.MemberResponse;

import java.util.List;

public interface MemberService {

  @Transactional
  Member create(CreateMemberRequest createMemberRequest);

  @Transactional
  MemberResponse add(AddMemberRequest addMemberRequest);

  @Transactional
  String delete(String memberId);

  @Transactional
  String deleteFromConversation(String memberId, Long conversationId);

  @Transactional(readOnly = true)
  List<MemberResponse> getConversationMembers(List<String> members);

  @Transactional(readOnly = true)
  Page<MemberResponse> searchForMembersByName(String name, String requesterId, Pageable pageable);
}
