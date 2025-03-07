package project.school.socialmedia.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.school.socialmedia.dao.Conversation;
import project.school.socialmedia.dao.Member;
import project.school.socialmedia.dao.MemberConversations;
import project.school.socialmedia.dto.request.member.AddMemberRequest;
import project.school.socialmedia.dto.request.member.CreateMemberRequest;
import project.school.socialmedia.dto.request.member.DeleteMemberRequest;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;

import java.util.NoSuchElementException;

@Service
public class MemberService {

  private final MemberRepository memberRepository;
  private final MemberConversationsRepository memberConversationsRepository;
  private final ConversationRepository conversationRepository;
  private static final String DELETE_SUCCESS = "Delete was successful!";
  private static final String MEMBER_NOT_FOUND = "Member not found!";
  private static final String CONVERSATION_NOT_FOUND = "Conversation not found!";



  @Autowired
  public MemberService(MemberRepository memberRepository,
                       MemberConversationsRepository memberConversationsRepository,
                       ConversationRepository conversationRepository
  ) {
    this.memberRepository = memberRepository;
    this.memberConversationsRepository = memberConversationsRepository;
    this.conversationRepository = conversationRepository;
  }

  @Transactional
  public Member create(CreateMemberRequest createMemberRequest) {
    String randomNameAvatar =
            "https://avatar.iran.liara.run/username?username=[" + createMemberRequest.getFirstName() + "+" + createMemberRequest.getLastName()+ "]";
    Member member = new Member(createMemberRequest.getId(), createMemberRequest.getFirstName(), createMemberRequest.getLastName(), randomNameAvatar);
    return memberRepository.save(member);
  }

  @Transactional
  public Member add(AddMemberRequest addMemberRequest) {
    Member member = findMember(addMemberRequest.getMemberId());
    Conversation conversation = findConversation(addMemberRequest.getConversationId());
    MemberConversations memberConversations = new MemberConversations(member, conversation);
    memberConversationsRepository.save(memberConversations);
    return member;
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
    return DELETE_SUCCESS;
  }

  @Transactional(readOnly = true)
  public Page<Member> getConversationMembers(long conversationId, Pageable pageable) {
    Page<MemberConversations> memberConversations = memberConversationsRepository.findByConversationId(conversationId, pageable);
    return memberConversations.map(MemberConversations::getMember);
  }

  private Member findMember(String memberId) {
    return memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException(MEMBER_NOT_FOUND));
  }

  private Conversation findConversation(long conversationId) {
    return conversationRepository.findById(conversationId).orElseThrow(() -> new NoSuchElementException(CONVERSATION_NOT_FOUND));
  }
}
