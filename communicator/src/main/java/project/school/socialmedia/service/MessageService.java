package project.school.socialmedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.school.socialmedia.configuration.KafkaConfigProps;
import project.school.socialmedia.dao.Message;
import project.school.socialmedia.dto.kafka.Notification;
import project.school.socialmedia.dto.request.message.CreateMessageRequest;
import project.school.socialmedia.dto.request.message.UpdateMessageRequest;
import project.school.socialmedia.dto.response.message.MessageResponse;
import project.school.socialmedia.dto.response.message.SimpleMessageResponse;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;
import project.school.socialmedia.repository.MessageRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

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

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final KafkaConfigProps kafkaConfigProps;

  private final ObjectMapper objectMapper;

  @Transactional(readOnly = true)
  public Page<MessageResponse> get(long conversationId, Pageable pageable) {
    if(!conversationRepository.existsById(conversationId)){
      throw new NoSuchElementException(NOT_FOUND_CONVERSATION);
    }
    return messageRepository.findByConversationIdOrderBySentAtDesc(conversationId, pageable)
            .map(message -> new MessageResponse(message.getId(), message.getMemberId(), message.getContent(), message.getSentAt().toString()));
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
  public MessageResponse create(CreateMessageRequest request) throws NoSuchElementException, JsonProcessingException {
    if(!isMember(request.getConversationId(), request.getSenderId())) {
      throw new IllegalArgumentException("The user is not a member of this conversation!");
    }

    if (request.getContent().isEmpty()) {
      throw new IllegalArgumentException("The message can not be empty!");
    }

    Message message = messageRepository.save(
            new Message(
                    request.getConversationId(),
                    request.getSenderId(),
                    LocalDateTime.parse(request.getSentAt()),
                    request.getContent()
            ));

    Notification notification = createNotification(request, message.getSentAt());
    sendNotificationWithKafka(notification);

    return new MessageResponse(message.getId(), message.getMemberId(), message.getContent(), message.getSentAt().toString());
  }

  private Message findMessage(long messageId) throws NoSuchElementException {
    return messageRepository.findById(messageId).orElseThrow(() -> new NoSuchElementException(NOT_FOUND_MESSAGE));
  }

  private boolean isMember(long conversationId, String memberId){
    return memberConversationsRepository.isMemberOfConversation(conversationId, memberId);
  }

  private Notification createNotification(CreateMessageRequest request, LocalDateTime sentAt) {
    String convName = conversationRepository.findById(request.getConversationId()).orElseThrow(NoSuchElementException::new).getName();
    String memberName = memberRepository.findById(request.getSenderId()).orElseThrow(NoSuchElementException::new).getFirstName();
    String notificationMessage = "You have a new message in "+convName+" from  "+memberName;

    List<String> receiverIds = memberConversationsRepository.findByConversationId(request.getConversationId())
            .stream()
            .map(mCs  -> mCs.getMember().getId())
            .filter(receiverId -> !receiverId.equals(request.getSenderId()))
            .toList();

    return Notification.builder()
            .createdAt(sentAt)
            .userIds(receiverIds)
            .message(notificationMessage)
            .build();
  }

  private void sendNotificationWithKafka(Notification notification) throws JsonProcessingException {
    final String payload = objectMapper.writeValueAsString(notification);
    kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
  }
}
