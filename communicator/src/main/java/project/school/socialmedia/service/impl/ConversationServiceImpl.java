package project.school.socialmedia.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.school.socialmedia.domain.Conversation;
import project.school.socialmedia.domain.Member;
import project.school.socialmedia.domain.MemberConversations;
import project.school.socialmedia.dto.request.conversation.CreateConversationRequest;
import project.school.socialmedia.dto.request.conversation.UpdateNamingRequest;
import project.school.socialmedia.dto.response.conversation.ConversationResponse;
import project.school.socialmedia.dto.response.conversation.SimpleConversationResponse;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;
import project.school.socialmedia.service.ConversationService;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

@Service
@AllArgsConstructor
public class ConversationServiceImpl implements ConversationService {

  private static final String NOT_FOUND_CONVERSATION = "Conversation not found!";
  private static final String INTEGRITY_CONSTRAINT_MESSAGE = "Invalid naming!";
  private static final String DELETE_SUCCESS = "Conversation was deleted";
  private final ConversationRepository conversationRepository;
  private final MemberRepository memberRepository;
  private final MemberConversationsRepository memberConversationsRepository;

  @Transactional(readOnly = true)
  public Conversation get(long conversationId) throws NoSuchElementException {
    return findConversation(conversationId);
  }

  @Transactional
  public String delete(long conversationId) {
    Conversation conversation = findConversation(conversationId);
    conversationRepository.delete(conversation);
    return DELETE_SUCCESS;
  }

  @Transactional
  public Conversation update(long conversationId, UpdateNamingRequest modifiedConversation) {
    Conversation conversation = findConversation(conversationId);
    conversation.setName(modifiedConversation.getName());
    return conversationRepository.save(conversation);
  }


  @Transactional
  public Conversation create(CreateConversationRequest createConversationRequest) throws SQLIntegrityConstraintViolationException {
    if (isNameValid(createConversationRequest.getName())) {
      Conversation conversation = conversationRepository.save(new Conversation(createConversationRequest.getName()));

      List<MemberConversations> memberConversationsList = Arrays.stream(createConversationRequest.getMembers()).map(memberId -> {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NoSuchElementException("Wrong user! This user is not registered!"));
        return new MemberConversations(member, conversation);
      }).toList();

      memberConversationsRepository.saveAll(memberConversationsList);

      return conversation;
    }
    throw new SQLIntegrityConstraintViolationException(INTEGRITY_CONSTRAINT_MESSAGE);
  }

  @Transactional(readOnly = true)
  public Page<SimpleConversationResponse> getMemberConversations(String memberId, Pageable pageable) {
    Page<MemberConversations> memberConversations = memberConversationsRepository.findByMemberId(memberId, pageable);
    return memberConversations.map(mCs -> new SimpleConversationResponse(mCs.getConversation().getId(), mCs.getConversation().getName()));
  }

  @Transactional(readOnly = true)
  public Page<SimpleConversationResponse> searchByName(String name, String memberId, Pageable pageable) {
    return conversationRepository.searchByNameForMember(name, memberId, pageable)
            .map(conversation -> new SimpleConversationResponse(conversation.getId(), conversation.getName()));
  }

  @Transactional(readOnly = true)
  public ConversationResponse getConversation(String conversationId) {
    Conversation conversation = conversationRepository.findById(Long.valueOf(conversationId)).orElseThrow(NoSuchElementException::new);
    List<String> memberIds = conversation.getMemberConversations().stream().map(mCs -> mCs.getMember().getId()).toList();
    return new ConversationResponse(conversationId, conversation.getName(), memberIds);
  }

  private boolean isNameValid(String name) {
    return !name.isEmpty();
  }

  private Conversation findConversation(long conversationId) throws NoSuchElementException {
    return conversationRepository.findById(conversationId).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_CONVERSATION));
  }
}
