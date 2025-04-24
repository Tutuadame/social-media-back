package project.school.socialmedia.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import project.school.socialmedia.domain.Conversation;
import project.school.socialmedia.dto.request.conversation.CreateConversationRequest;
import project.school.socialmedia.dto.request.conversation.UpdateNamingRequest;
import project.school.socialmedia.dto.response.conversation.ConversationResponse;
import project.school.socialmedia.dto.response.conversation.SimpleConversationResponse;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.NoSuchElementException;

public interface ConversationService {

  @Transactional(readOnly = true)
  Conversation get(long conversationId) throws NoSuchElementException;

  @Transactional
  String delete(long conversationId);

  @Transactional
  Conversation update(long conversationId, UpdateNamingRequest modifiedConversation);

  @Transactional
  Conversation create(CreateConversationRequest createConversationRequest) throws SQLIntegrityConstraintViolationException;

  @Transactional(readOnly = true)
  Page<SimpleConversationResponse> getMemberConversations(String memberId, Pageable pageable);

  @Transactional(readOnly = true)
  Page<SimpleConversationResponse> searchByName(String name, String memberId, Pageable pageable);

  @Transactional(readOnly = true)
  ConversationResponse getConversation(String conversationId);
}
