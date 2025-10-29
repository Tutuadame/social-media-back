package project.school.socialmedia.controller;

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
import project.school.socialmedia.domain.Conversation;
import project.school.socialmedia.dto.request.conversation.CreateConversationRequest;
import project.school.socialmedia.dto.request.conversation.UpdateNamingRequest;
import project.school.socialmedia.dto.response.conversation.ConversationResponse;
import project.school.socialmedia.dto.response.conversation.SimpleConversationResponse;
import project.school.socialmedia.service.impl.ConversationServiceImpl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConversationControllerTest {

  @Mock
  private ConversationServiceImpl conversationService;

  @InjectMocks
  private ConversationController conversationController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void deleteConversation_ReturnsNoContent() {
    // Arrange
    long conversationId = 1L;
    when(conversationService.delete(conversationId)).thenReturn("Conversation was deleted");

    // Act
    ResponseEntity<String> response = conversationController.deleteConversation(conversationId);

    // Assert
    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertEquals("Conversation was deleted", response.getBody());
    verify(conversationService).delete(conversationId);
  }

  @Test
  void updateConversationName_ReturnsUpdatedConversation() {
    // Arrange
    long conversationId = 1L;
    UpdateNamingRequest request = new UpdateNamingRequest();
    request.setName("Updated Conversation");

    Conversation updatedConversation = new Conversation();
    updatedConversation.setId(conversationId);
    updatedConversation.setName("Updated Conversation");

    when(conversationService.update(conversationId, request)).thenReturn(updatedConversation);

    // Act
    ResponseEntity<SimpleConversationResponse> response =
            conversationController.updateConversationName(conversationId, request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    SimpleConversationResponse result = response.getBody();
    assertNotNull(result);
    assertEquals(conversationId, result.getId());
    assertEquals("Updated Conversation", result.getName());
    verify(conversationService).update(conversationId, request);
  }

  @Test
  void createConversation_ReturnsNewConversation() throws SQLIntegrityConstraintViolationException {
    // Arrange
    CreateConversationRequest request = new CreateConversationRequest();
    request.setName("New Conversation");
    request.setMembers(new String[]{"member1", "member2"});

    Conversation newConversation = new Conversation();
    newConversation.setId(1L);
    newConversation.setName("New Conversation");

    when(conversationService.create(request)).thenReturn(newConversation);

    // Act
    ResponseEntity<SimpleConversationResponse> response =
            conversationController.createConversation(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    SimpleConversationResponse result = response.getBody();
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("New Conversation", result.getName());
    verify(conversationService).create(request);
  }

  @Test
  void getConversation_ReturnsConversationDetails() {
    // Arrange
    String conversationId = "1";
    List<String> memberIds = Arrays.asList("member1", "member2");
    ConversationResponse expectedResponse = new ConversationResponse(conversationId, "Test Conversation", memberIds);

    when(conversationService.getConversation(conversationId)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<ConversationResponse> response = conversationController.getConversation(conversationId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    ConversationResponse result = response.getBody();
    assertNotNull(result);
    assertEquals(conversationId, result.getId());
    assertEquals("Test Conversation", result.getName());
    assertEquals(memberIds, result.getMemberIds());
    verify(conversationService).getConversation(conversationId);
  }

  @Test
  void getConversations_ReturnsPageOfConversations() {
    // Arrange
    String memberId = "member1";
    int pageNumber = 0;
    int pageSize = 10;

    List<SimpleConversationResponse> conversations = new ArrayList<>();
    conversations.add(new SimpleConversationResponse(1L, "Conversation 1"));
    conversations.add(new SimpleConversationResponse(2L, "Conversation 2"));

    Page<SimpleConversationResponse> conversationPage = new PageImpl<>(conversations);

    when(conversationService.getMemberConversations(eq(memberId), any(Pageable.class)))
            .thenReturn(conversationPage);

    // Act
    ResponseEntity<Page<SimpleConversationResponse>> response =
            conversationController.getConversations(memberId, pageSize, pageNumber);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<SimpleConversationResponse> result = response.getBody();
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    verify(conversationService).getMemberConversations(eq(memberId), any(Pageable.class));
  }

  @Test
  void searchForConversations_ReturnsMatchingConversations() {
    // Arrange
    String searchName = "Test";
    String requesterId = "member1";
    int pageSize = 10;
    int pageNumber = 0;

    List<SimpleConversationResponse> conversations = new ArrayList<>();
    conversations.add(new SimpleConversationResponse(1L, "Test Conversation 1"));
    conversations.add(new SimpleConversationResponse(2L, "Test Conversation 2"));

    Page<SimpleConversationResponse> conversationPage = new PageImpl<>(conversations);

    when(conversationService.searchByName(eq(searchName), eq(requesterId), any(Pageable.class)))
            .thenReturn(conversationPage);

    // Act
    ResponseEntity<Page<SimpleConversationResponse>> response =
            conversationController.searchForConversations(searchName, requesterId, pageSize, pageNumber);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<SimpleConversationResponse> result = response.getBody();
    assertNotNull(result);
    assertEquals(2, result.getContent().size());
    verify(conversationService).searchByName(eq(searchName), eq(requesterId), any(Pageable.class));
  }
}