package project.school.socialmedia.controller;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.domain.Conversation;
import project.school.socialmedia.dto.request.conversation.CreateConversationRequest;
import project.school.socialmedia.dto.request.conversation.UpdateNamingRequest;
import project.school.socialmedia.dto.response.conversation.ConversationResponse;
import project.school.socialmedia.dto.response.conversation.SimpleConversationResponse;
import project.school.socialmedia.service.impl.ConversationServiceImpl;

import java.sql.SQLIntegrityConstraintViolationException;

@RestController
@RequestMapping("/conversationApi")
@AllArgsConstructor
public class ConversationController {

  private final ConversationServiceImpl conversationServiceImpl;

  @DeleteMapping("/{conversationId}")
  public ResponseEntity<String> deleteConversation(
          @PathVariable long conversationId
  ) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            conversationServiceImpl.delete(conversationId)
    );
  }

  @PatchMapping("/naming/{conversationId}")
  public ResponseEntity<SimpleConversationResponse> updateConversationName (
          @PathVariable long conversationId,
          @RequestBody UpdateNamingRequest updateNamingRequest
  ) {
    Conversation conversation = conversationServiceImpl.update(conversationId, updateNamingRequest);
    SimpleConversationResponse conversationResponse = new SimpleConversationResponse(conversation.getId(), conversation.getName());
    return ResponseEntity.status(HttpStatus.OK).body(conversationResponse);
  }

  @PostMapping("/new")
  public ResponseEntity<SimpleConversationResponse> createConversation (
          @RequestBody CreateConversationRequest createConversationRequest
  ) throws SQLIntegrityConstraintViolationException {
    Conversation conversation =  conversationServiceImpl.create(createConversationRequest);
    SimpleConversationResponse conversationResponse = new SimpleConversationResponse(conversation.getId(), conversation.getName());
    return ResponseEntity.status(HttpStatus.CREATED).body(conversationResponse);
  }

  @GetMapping("/conversations/{conversationId}")
  public ResponseEntity<ConversationResponse> getConversation (@PathVariable String conversationId) {
    ConversationResponse conversation = conversationServiceImpl.getConversation(conversationId);
    return ResponseEntity.status(200).body(conversation);
  }

  @GetMapping("/conversations")
  public ResponseEntity<Page<SimpleConversationResponse>> getConversations (
          @RequestParam String memberId,
          @RequestParam int pageSize,
          @RequestParam int pageNumber
  ) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<SimpleConversationResponse> conversationPage = conversationServiceImpl.getMemberConversations(memberId, pageable);
    return ResponseEntity.status(200).body(conversationPage);
  }

  //Chat Management Flow
  @GetMapping("/search")
  public ResponseEntity<Page<SimpleConversationResponse>> searchForConversations(
          @RequestParam String name,
          @RequestParam String requesterId,
          @RequestParam int pageSize,
          @RequestParam int pageNumber
  ) {
    Pageable pageable = PageRequest.of(pageNumber, pageSize);
    Page<SimpleConversationResponse> conversationResponse = conversationServiceImpl.searchByName(
            name,
            requesterId,
            pageable
    );
    return ResponseEntity.status(HttpStatus.OK).body(conversationResponse);
  }
}
