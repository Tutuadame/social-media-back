package project.school.socialmedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import project.school.socialmedia.configuration.KafkaConfigProps;
import project.school.socialmedia.domain.Conversation;
import project.school.socialmedia.domain.Member;
import project.school.socialmedia.domain.MemberConversations;
import project.school.socialmedia.domain.Message;
import project.school.socialmedia.dto.kafka.Notification;
import project.school.socialmedia.dto.request.message.CreateMessageRequest;
import project.school.socialmedia.dto.request.message.UpdateMessageRequest;
import project.school.socialmedia.dto.response.message.MessageResponse;
import project.school.socialmedia.dto.response.message.SimpleMessageResponse;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;
import project.school.socialmedia.repository.MessageRepository;
import project.school.socialmedia.service.impl.MessageServiceImpl;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MessageServiceImplTest {

  @Mock
  private MessageRepository messageRepository;

  @Mock
  private MemberConversationsRepository memberConversationsRepository;

  @Mock
  private ConversationRepository conversationRepository;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private KafkaConfigProps kafkaConfigProps;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private MessageServiceImpl messageService;

  private Member testMember;
  private Conversation testConversation;
  private Message testMessage;
  private LocalDateTime now;
  private final Pageable pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    now = LocalDateTime.now();

    // Setup test member
    testMember = new Member("member1", "John", "Doe", "picture.jpg");

    // Setup test conversation
    testConversation = new Conversation();
    testConversation.setId(1L);
    testConversation.setName("Test Conversation");

    // Setup test message
    testMessage = new Message();
    testMessage.setId(1L);
    testMessage.setMember(testMember);
    testMessage.setConversation(testConversation);
    testMessage.setContent("Hello, world!");
    testMessage.setSentAt(now);

    when(kafkaConfigProps.getTopic()).thenReturn("notifications");
  }

  @Test
  void get_ExistingConversation_ReturnsMessages() {
    // Arrange
    long conversationId = 1L;
    List<Message> messages = Collections.singletonList(testMessage);
    Page<Message> messagePage = new PageImpl<>(messages);

    when(conversationRepository.existsById(conversationId)).thenReturn(true);
    when(messageRepository.findByConversation_IdOrderBySentAtDesc(eq(conversationId), any(Pageable.class)))
            .thenReturn(messagePage);

    // Act
    Page<MessageResponse> result = messageService.get(conversationId, pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(1L, result.getContent().getFirst().getId());
    assertEquals("member1", result.getContent().getFirst().getSenderId());
    assertEquals("Hello, world!", result.getContent().getFirst().getContent());
    verify(conversationRepository).existsById(conversationId);
    verify(messageRepository).findByConversation_IdOrderBySentAtDesc(eq(conversationId), any(Pageable.class));
  }

  @Test
  void get_NonExistingConversation_ThrowsException() {
    // Arrange
    long conversationId = 999L;
    when(conversationRepository.existsById(conversationId)).thenReturn(false);

    // Act & Assert
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      messageService.get(conversationId, pageable);
    });
    assertEquals("Conversation not found!", exception.getMessage());
    verify(conversationRepository).existsById(conversationId);
    verify(messageRepository, never()).findByConversation_IdOrderBySentAtDesc(anyLong(), any(Pageable.class));
  }

  @Test
  void delete_ExistingMessage_ReturnsSuccessMessage() {
    // Arrange
    long messageId = 1L;
    when(messageRepository.findById(messageId)).thenReturn(Optional.of(testMessage));
    doNothing().when(messageRepository).delete(testMessage);

    // Act
    String result = messageService.delete(messageId);

    // Assert
    assertEquals("Message was deleted!", result);
    verify(messageRepository).findById(messageId);
    verify(messageRepository).delete(testMessage);
  }

  @Test
  void delete_NonExistingMessage_ThrowsException() {
    // Arrange
    long messageId = 999L;
    when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

    // Act & Assert
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      messageService.delete(messageId);
    });
    assertEquals("Message not found!", exception.getMessage());
    verify(messageRepository).findById(messageId);
    verify(messageRepository, never()).delete(any(Message.class));
  }

  @Test
  void update_ExistingMessage_ReturnsUpdatedMessage() {
    // Arrange
    long messageId = 1L;
    UpdateMessageRequest request = new UpdateMessageRequest();
    request.setMessageContent("Updated message");

    Message updatedMessage = new Message();
    updatedMessage.setId(messageId);
    updatedMessage.setContent("Updated message");
    updatedMessage.setSentAt(now);

    when(messageRepository.findById(messageId)).thenReturn(Optional.of(testMessage));
    when(messageRepository.save(any(Message.class))).thenReturn(updatedMessage);

    // Act
    SimpleMessageResponse result = messageService.update(messageId, request);

    // Assert
    assertNotNull(result);
    assertEquals(messageId, result.getId());
    assertEquals("Updated message", result.getContent());
    verify(messageRepository).findById(messageId);
    verify(messageRepository).save(any(Message.class));
  }

  @Test
  void create_ValidRequest_ReturnsNewMessage() throws JsonProcessingException {
    // Arrange
    CreateMessageRequest request = new CreateMessageRequest();
    request.setSenderId("member1");
    request.setConversationId(1L);
    request.setContent("Hello, world!");

    Member member2 = new Member("member2", "Jane", "Smith", "picture2.jpg");
    MemberConversations mc1 = new MemberConversations(1L, testMember, testConversation);
    MemberConversations mc2 = new MemberConversations(2L, member2, testConversation);
    List<MemberConversations> memberConversations = Arrays.asList(mc1, mc2);

    when(memberConversationsRepository.isMemberOfConversation(1L, "member1")).thenReturn(true);
    when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
    when(memberRepository.findById("member1")).thenReturn(Optional.of(testMember));
    when(messageRepository.save(any(Message.class))).thenReturn(testMessage);
    when(memberConversationsRepository.findByConversation_Id(1L)).thenReturn(memberConversations);
    when(objectMapper.writeValueAsString(any(Notification.class))).thenReturn("serialized notification");

    // Act
    MessageResponse result = messageService.create(request);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("member1", result.getSenderId());
    assertEquals("Hello, world!", result.getContent());
    verify(memberConversationsRepository).isMemberOfConversation(1L, "member1");
    verify(conversationRepository, times(2)).findById(1L);
    verify(memberRepository, times(2)).findById("member1");
    verify(messageRepository).save(any(Message.class));
    verify(kafkaTemplate).send(eq("notifications"), eq("serialized notification"));
  }

  @Test
  void create_NotAMember_ThrowsException() {
    // Arrange
    CreateMessageRequest request = new CreateMessageRequest();
    request.setSenderId("member1");
    request.setConversationId(1L);
    request.setContent("Hello, world!");

    when(memberConversationsRepository.isMemberOfConversation(1L, "member1")).thenReturn(false);

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      messageService.create(request);
    });
    assertEquals("The user is not a member of this conversation!", exception.getMessage());
    verify(memberConversationsRepository).isMemberOfConversation(1L, "member1");
    verify(conversationRepository, never()).findById(anyLong());
  }

  @Test
  void create_EmptyContent_ThrowsException() {
    // Arrange
    CreateMessageRequest request = new CreateMessageRequest();
    request.setSenderId("member1");
    request.setConversationId(1L);
    request.setContent("");

    when(memberConversationsRepository.isMemberOfConversation(1L, "member1")).thenReturn(true);
    when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
    when(memberRepository.findById("member1")).thenReturn(Optional.of(testMember));

    // Act & Assert
    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
      messageService.create(request);
    });
    assertEquals("The message can not be empty!", exception.getMessage());
    verify(memberConversationsRepository).isMemberOfConversation(1L, "member1");
    verify(conversationRepository).findById(1L);
    verify(memberRepository).findById("member1");
    verify(messageRepository, never()).save(any(Message.class));
  }
}