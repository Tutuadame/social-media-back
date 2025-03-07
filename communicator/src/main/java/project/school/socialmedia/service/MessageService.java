package project.school.socialmedia.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.school.socialmedia.dao.Conversation;
import project.school.socialmedia.dao.Member;
import project.school.socialmedia.dao.Message;
import project.school.socialmedia.dto.request.message.CreateMessageRequest;
import project.school.socialmedia.dto.request.message.UpdateMessageRequest;
import project.school.socialmedia.dto.response.message.MessageResponse;
import project.school.socialmedia.dto.response.message.SimpleMessageResponse;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;
import project.school.socialmedia.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@AllArgsConstructor
public class MessageService {

  private static final String NOT_FOUND_MESSAGE = "Message not found!";
  private static final String NOT_FOUND_CONVERSATION = "Conversation not found!";
  private static final String DELETE_SUCCESS = "Message was deleted!";
  private final MessageRepository messageRepository;
  private final MemberConversationsRepository memberConversationsRepository;
  private final ConversationRepository conversationRepository;
  private final MemberRepository memberRepository;

  @Transactional(readOnly = true)
  public Page<MessageResponse> get(long conversationId, Pageable pageable) {
    if(!conversationRepository.existsById(conversationId)){
      throw new NoSuchElementException(NOT_FOUND_CONVERSATION);
    }
    return messageRepository.findByConversationIdOrderBySentAtDesc(conversationId, pageable)
            .map(message -> {
              Member member = memberRepository.findById(message.getMemberId()).orElseThrow(NoSuchElementException::new);
              return new MessageResponse(
                      message.getId(),
                      message.getMemberId(),
                      member.getFirstName(),
                      member.getLastName(),
                      member.getPicture(),
                      message.getContent(),
                      message.getSentAt().toString()
              );
            });
  }

  @Transactional
  public String delete(long messageId) throws NoSuchElementException {
    Message message = findMessage(messageId);
    messageRepository.delete(message);
    return DELETE_SUCCESS;
  }

  @Transactional
  public SimpleMessageResponse update(long messageId, UpdateMessageRequest updateMessageRequest) throws NoSuchElementException {
    Message message = findMessage(messageId);
    message.setContent(updateMessageRequest.getMessageContent());
    messageRepository.save(message);
    return new SimpleMessageResponse(message.getId(), message.getContent(), message.getSentAt().toString());
  }

  @Transactional
  public SimpleMessageResponse create(CreateMessageRequest request) throws NoSuchElementException {
    if(!isMember(request.getConversationId(), request.getSenderId())){
      throw new IllegalArgumentException("The user is not a member of this conversation!");
    }
    LocalDateTime messageSentAt = LocalDateTime.parse(request.getSentAt());
    Message message = messageRepository.save(new Message(request.getConversationId(), request.getSenderId(), messageSentAt, request.getContent()));
    return new SimpleMessageResponse(message.getId(), message.getContent(), message.getSentAt().toString());
  }

  private Message findMessage(long messageId) throws NoSuchElementException {
    return messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));
  }

  private boolean isMember(long conversationId, String memberId){
    return memberConversationsRepository.isMemberOfConversation(conversationId, memberId);
  }
}
