package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.dto.request.message.CreateMessageRequest;
import project.school.socialmedia.dto.request.message.UpdateMessageRequest;
import project.school.socialmedia.dto.response.message.MessageResponse;
import project.school.socialmedia.dto.response.message.SimpleMessageResponse;
import project.school.socialmedia.service.impl.MessageServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MessageControllerTest {

  @Mock
  private MessageServiceImpl messageService;

  @InjectMocks
  private MessageController messageController;

  private MessageResponse testMessageResponse;

  private SimpleMessageResponse testSimpleMessageResponse;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Setup test message response
    testMessageResponse = new MessageResponse(1L, "member1", "Hello, world!", LocalDateTime.now().toString());

    // Setup test simple message response
    testSimpleMessageResponse = new SimpleMessageResponse(1L, "Hello, world!", LocalDateTime.now().toString());
  }

  @Test
  void updateMessage_ReturnsUpdatedMessage() {
    // Arrange
    long messageId = 1L;
    UpdateMessageRequest request = new UpdateMessageRequest();
    request.setMessageContent("Updated message");

    SimpleMessageResponse updatedResponse = new SimpleMessageResponse(messageId, "Updated message", LocalDateTime.now().toString());
    when(messageService.update(messageId, request)).thenReturn(updatedResponse);

    // Act
    ResponseEntity<SimpleMessageResponse> response = messageController.updateMessage(messageId, request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    SimpleMessageResponse result = response.getBody();
    assertNotNull(result);
    assertEquals(messageId, result.getId());
    assertEquals("Updated message", result.getContent());
    verify(messageService).update(messageId, request);
  }

  @Test
  void createMessage_ReturnsCreatedMessage() throws JsonProcessingException {
    // Arrange
    CreateMessageRequest request = new CreateMessageRequest();
    request.setSenderId("member1");
    request.setConversationId(1L);
    request.setContent("Hello, world!");

    when(messageService.create(request)).thenReturn(testMessageResponse);

    // Act
    ResponseEntity<MessageResponse> response = messageController.createMessage(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    MessageResponse result = response.getBody();
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("member1", result.getSenderId());
    assertEquals("Hello, world!", result.getContent());
    verify(messageService).create(request);
  }

  @Test
  void deleteMessage_ReturnsNoContent() {
    // Arrange
    long messageId = 1L;
    when(messageService.delete(messageId)).thenReturn("Message was deleted!");

    // Act
    ResponseEntity<String> response = messageController.deleteMessage(messageId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("Message was deleted!", response.getBody());
    verify(messageService).delete(messageId);
  }

  @Test
  void getConversationMessages_ReturnsPageOfMessages() {
    // Arrange
    long conversationId = 1L;
    int pageNumber = 0;
    int pageSize = 10;

    List<MessageResponse> messages = new ArrayList<>();
    messages.add(testMessageResponse);
    Page<MessageResponse> messagePage = new PageImpl<>(messages);

    when(messageService.get(eq(conversationId), any(Pageable.class))).thenReturn(messagePage);

    // Act
    ResponseEntity<Page<MessageResponse>> response =
            messageController.getConversationMessages(conversationId, pageNumber, pageSize);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<MessageResponse> result = response.getBody();
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(1L, result.getContent().getFirst().getId());
    assertEquals("Hello, world!", result.getContent().getFirst().getContent());
    verify(messageService).get(eq(conversationId), any(Pageable.class));
  }
}