package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.dao.Message;
import project.school.socialmedia.dto.request.message.CreateMessageRequest;
import project.school.socialmedia.dto.request.message.UpdateMessageRequest;
import project.school.socialmedia.dto.response.message.MessageResponse;
import project.school.socialmedia.service.MessageService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class MessageControllerTest {
/*
  @Mock
  private MessageService messageService;
  @InjectMocks
  private MessageController messageController;

  @BeforeEach
  void setup() {
    this.messageController = new MessageController(messageService);
  }

  @AfterEach
  void reset() {
    this.messageController = null;
  }

  @Test
  @DisplayName("Should create message with appropriate fields!")
  void shouldCreateMessageWithCorrectFields() throws JsonProcessingException {
    //Arrange
    long conversationId = 0L;
    String senderId = "1";
    String content = "test content";
    String localDateTime = LocalDateTime.now().toString();
    CreateMessageRequest createMessageRequest = new CreateMessageRequest(conversationId, content, senderId, localDateTime);
    MessageResponse messageResponse = new MessageResponse();
    ResponseEntity<MessageResponse> responseEntity = ResponseEntity.status(201).body(messageResponse);

    when(messageService.create(any(CreateMessageRequest.class))).thenReturn(messageResponse);

    //Act
    ResponseEntity<MessageResponse> messageResponseEntity = messageController.createMessage(createMessageRequest);

    //Assert
    assertEquals(null, responseEntity, messageResponseEntity);
  }

  @Test
  @DisplayName("Should when create message when conversation entity not found!")
  void shouldNotCreateMessageWhenConversationNotFound() {
    long conversationId = 0L;
    String senderId = "1";
    String content = "test content";
    String localDateTime = LocalDateTime.now().toString();
    CreateMessageRequest createMessageRequest = new CreateMessageRequest(conversationId, content, senderId, localDateTime);
    when(messageService.create(any(CreateMessageRequest.class))).thenThrow(new NoSuchElementException("test exception"));

    assertThrows(NoSuchElementException.class, () -> messageController.createMessage(createMessageRequest));
  }

  @Test
  @DisplayName("Should return a messages when conversation exists!")
  void shouldGetMessagesWhenConversationExists() {
    //Arrange
    long conversationId = 0L;
    List<MessageResponse> messageList = List.of(new MessageResponse());
    Page<MessageResponse> pageable = new PageImpl<>(messageList, PageRequest.of(0, 10), messageList.size());
    ResponseEntity<Page<MessageResponse>> responseEntity = ResponseEntity.status(200).body(pageable);

    when(messageService.get(anyLong(), any(Pageable.class))).thenReturn(pageable);

    //Act
    ResponseEntity<Page<MessageResponse>> conversationResponseResponseEntity = messageController.getConversationMessages(conversationId, 0, 10);

    //Assert
    assertEquals(null, responseEntity, conversationResponseResponseEntity);
  }

  @Test
  @DisplayName("Should throw exception when there is no conversation with the id during get messages")
  void ShouldThrowExceptionWhenThereIsNoConversationWhileGettingMessages() {
    //Arrange
    long conversationId = 0L;
    int pageNumber = 0;
    int pageSize = 10;
    when(messageService.get(anyLong(), any(Pageable.class))).thenThrow(new NoSuchElementException("test exception"));

    assertThrows(NoSuchElementException.class, () -> messageController.getConversationMessages(conversationId, pageNumber, pageSize));
  }

  @Test
  @DisplayName("Should delete message when ID is correct")
  void shouldDeleteMessageWhenIdCorrect() {
    //Arrange
    long messageId = 0L;
    String deleteMessage = "test delete";
    when(messageService.delete(anyLong())).thenReturn(deleteMessage);
    ResponseEntity<String> responseEntity = ResponseEntity.status(204).body(deleteMessage);

    //Act
    ResponseEntity<String> result = messageController.deleteMessage(messageId);

    //Assert
    assertEquals(null, responseEntity, result);
  }

  @Test
  @DisplayName("Should not delete when entity is not found!")
  void shouldNotDeleteMessageWhenEntityNotFound() {
    //Arrange
    long messageId = 0;
    when(messageService.delete(anyLong())).thenThrow(new NoSuchElementException("test exception"));

    assertThrows(NoSuchElementException.class, () -> messageController.deleteMessage(messageId));
  }


  @Test
  @DisplayName("Should update message when message details are correct")
  void shouldUpdateMessageWhenDetailsCorrect() {
    //Arrange
    long messageId = 0L;
    String contentNew = "new";
    MessageResponse messageResponse = new MessageResponse();
    UpdateMessageRequest updateMessageRequest = new UpdateMessageRequest(contentNew);
    ResponseEntity<MessageResponse> responseEntity = ResponseEntity.status(200).body(messageResponse);
    when(messageService.update(anyLong(), any(UpdateMessageRequest.class))).thenReturn(messageResponse);

    //Act
    ResponseEntity<MessageResponse> result = messageController.updateMessage(messageId, updateMessageRequest);

    //Assert
    assertEquals(null, responseEntity, result);
  }


  @Test
  @DisplayName("Should throw exception during update when entity not found!")
  void shouldNotUpdateMessageEntityNotFound() {
    long messageId = 0L;
    UpdateMessageRequest updateMessageRequest = new UpdateMessageRequest("");
    when(messageService.update(anyLong(), any(UpdateMessageRequest.class))).thenThrow(new NoSuchElementException("test exception"));

    assertThrows(NoSuchElementException.class, () -> messageController.updateMessage(messageId, updateMessageRequest));
  }*/
}
