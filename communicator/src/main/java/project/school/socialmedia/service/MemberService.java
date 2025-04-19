package project.school.socialmedia.service;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import project.school.socialmedia.dao.Conversation;
import project.school.socialmedia.dao.Member;
import project.school.socialmedia.dao.MemberConversations;
import project.school.socialmedia.dto.request.member.AddMemberRequest;
import project.school.socialmedia.dto.request.member.CheckConnectionsBatchRequest;
import project.school.socialmedia.dto.request.member.CreateMemberRequest;
import project.school.socialmedia.dto.response.member.CheckConnectionsBatchResponse;
import project.school.socialmedia.dto.response.member.MemberResponse;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberConversationsRepository memberConversationsRepository;
  private final ConversationRepository conversationRepository;
  private final RestTemplate restTemplate;
  private static final String DELETE_SUCCESS = "Delete was successful!";
  private static final String DELETE_WITH_CONVERSATION_SUCCESS = "Member and Conversation was successfully deleted!";
  private static final String MEMBER_NOT_FOUND = "Member not found!";
  private static final String CONVERSATION_NOT_FOUND = "Conversation not found!";

  @Transactional
  public Member create(CreateMemberRequest createMemberRequest) {
    String randomNameAvatar =
            "https://avatar.iran.liara.run/username?username=" + createMemberRequest.getFirstName() + "+" + createMemberRequest.getLastName();
    Member member = new Member(createMemberRequest.getMemberId(), createMemberRequest.getFirstName(), createMemberRequest.getLastName(), randomNameAvatar);
    return memberRepository.save(member);
  }

  @Transactional
  public MemberResponse add(AddMemberRequest addMemberRequest) {
    Member member = findMember(addMemberRequest.getMemberId());
    Conversation conversation = findConversation(addMemberRequest.getConversationId());
    MemberConversations memberConversations = new MemberConversations(member, conversation);
    memberConversationsRepository.save(memberConversations);

    return new MemberResponse(member.getId(), member.getFirstName(), member.getLastName(), member.getPicture());
  }

  @Transactional
  public String delete(String memberId) {
    Member member = findMember(memberId);
    memberRepository.delete(member);
    memberConversationsRepository.deleteByMemberId(memberId);
    return DELETE_SUCCESS;
  }

  @Transactional
  public String deleteFromConversation(String memberId, Long conversationId) {
    memberConversationsRepository.deleteByMemberIdAndConversationId(memberId, conversationId);
    List<MemberConversations> memberConversations = memberConversationsRepository.findByConversationId(conversationId);
    if (memberConversations.isEmpty()) {
      conversationRepository.deleteById(conversationId);
      return DELETE_WITH_CONVERSATION_SUCCESS;
    }
    return DELETE_SUCCESS;
  }

  @Transactional(readOnly = true)
  public List<MemberResponse> getConversationMembers(List<String> members) {
    List<Member> memberList = memberRepository.findAllById(members);
    return memberList.stream().map(member -> new MemberResponse(member.getId(), member.getFirstName(), member.getLastName(), member.getPicture())).toList();
  }

  @Transactional(readOnly = true)
  public Page<MemberResponse> searchForMembersByName(String name, String requesterId, Pageable pageable) {
    Page<Member> membersPage = memberRepository.searchByName(name, pageable);

    List<Member> memberList = membersPage.getContent();
    List<String> targetIds = memberList.stream()
            .map(Member::getId)
            .filter(id -> !id.equals(requesterId))
            .toList();

    List<String> connectedIds = getConnectedMemberIds(requesterId, targetIds);

    List<MemberResponse> filteredResponses = memberList.stream()
            .filter(member -> connectedIds.contains(member.getId()))
            .map(member -> new MemberResponse(
                    member.getId(),
                    member.getFirstName(),
                    member.getLastName(),
                    member.getPicture()
            ))
            .toList();

    return new PageImpl<>(filteredResponses, pageable, filteredResponses.size());
  }


  private Member findMember(String memberId) {
    return memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException(MEMBER_NOT_FOUND));
  }

  private Conversation findConversation(long conversationId) {
    return conversationRepository.findById(conversationId).orElseThrow(() -> new NoSuchElementException(CONVERSATION_NOT_FOUND));
  }

  private List<String> getConnectedMemberIds(String requesterId, List<String> targetIds) {
    String url = "https://social.media:8445/connectionApi/checkConnectionsBatch";
    CheckConnectionsBatchRequest request = new CheckConnectionsBatchRequest(requesterId, targetIds);

    try {
      ResponseEntity<CheckConnectionsBatchResponse> response =
              restTemplate.postForEntity(url, request, CheckConnectionsBatchResponse.class);

      return response.getBody() != null ? response.getBody().getConnectedIds() : List.of();
    } catch (Exception e) {
      // log error
      return List.of();
    }
  }

}
