package project.school.socialmedia.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import project.school.socialmedia.dao.Conversation;
import project.school.socialmedia.dto.request.conversation.CreateConversationRequest;
import project.school.socialmedia.dto.response.conversation.SimpleConversationResponse;
import project.school.socialmedia.service.ConversationService;

import java.sql.SQLIntegrityConstraintViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest
class ConversationControllerTest {

  @Mock
  private ConversationService conversationService;
  @InjectMocks
  private ConversationController conversationController;

  @BeforeEach
  void setup() {
    this.conversationController = new ConversationController(conversationService);
  }

  @AfterEach
  void reset() {
    this.conversationController = null;
  }

  @Test
  @DisplayName("Should create a new conversation when a proper name is provided!")
  void shouldCreateConversationWhenThereIsAName() throws SQLIntegrityConstraintViolationException {
    String testName = "test";
    String[] memberIds = {};
    CreateConversationRequest createConversationRequest = new CreateConversationRequest(memberIds, testName);
    Conversation conversation = new Conversation(testName);
    SimpleConversationResponse simpleConversationResponse = new SimpleConversationResponse(conversation.getId(), testName);
    ResponseEntity<SimpleConversationResponse> conversationResponseEntity = ResponseEntity.status(201).body(simpleConversationResponse);
    when(conversationService.create(any(CreateConversationRequest.class))).thenReturn(conversation);

    ResponseEntity<SimpleConversationResponse> result = conversationController.createConversation(createConversationRequest);

    assertEquals(conversationResponseEntity, result);
  }

  @Test
  @DisplayName("Should throw an exception for wrong while creating a new conversation!")
  void shouldNotCreateConversationWhenNamingIsNotCorrect() throws SQLIntegrityConstraintViolationException {
    String testName = "test";
    String[] memberIds = {};
    CreateConversationRequest createConversationRequest = new CreateConversationRequest(memberIds, testName);
    when(conversationService.create(any(CreateConversationRequest.class))).thenThrow(new SQLIntegrityConstraintViolationException("test"));

    assertThrows(SQLIntegrityConstraintViolationException.class, () -> {
      conversationController.createConversation(createConversationRequest);
    });
  }

  @Test
  @DisplayName("Conversation should be deleted when entity exists!")
  void shouldDeleteConversationWhenIdCorrect() {
    long conversationId = 0L;
    String message = "Test delete";
    when(conversationService.delete(anyLong())).thenReturn(message);
    ResponseEntity<String> responseEntity = ResponseEntity.status(204).body(message);

    ResponseEntity<String> result = conversationController.deleteConversation(conversationId);

    assertEquals(responseEntity, result);
  }

  /*@Test
  @DisplayName("Conversation should be updated when entity exists and naming is correct!")
  void shouldUpdateConversationWhenEverythingIsCorrect() {
    long conversationId = 0L;
    String newName = "new test conv";
    Conversation changedConversation = new Conversation(newName);
    Conversation result = new Conversation();
    SimpleConversationResponse
    ResponseEntity<SimpleConversationResponse> responseEntity = ResponseEntity.status(200).body(result);

    when(conversationService.get(anyLong())).thenReturn(oldConversation);
    when(conversationService.update(anyLong(), any(UpdateConversation.class))).thenReturn(result);


    ResponseEntity<SimpleConversationResponse> result = conversationController.updateConversation(conversationId, changedConversation);

    assertEquals(responseEntity, result);
  }*/

  /*@Test
  @DisplayName("Conversation should not be updated when entity does not exist!")
  void shouldNotUpdateConversationWhenEntityNotFound() {
    long conversationId = 0L;
    String newName = "new test conv";
    Conversation changedConversation = new Conversation(newName);

    when(conversationService.update(anyLong(), any(Conversation.class))).thenThrow(new NoSuchElementException(""));

    assertThrows(NoSuchElementException.class, () -> {
      conversationController.updateConversation(conversationId, changedConversation);
    });
  }*/
}
