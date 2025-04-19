package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.dto.request.message.CreateMessageRequest;
import project.school.socialmedia.dto.request.message.GetMessagesRequest;
import project.school.socialmedia.dto.request.message.UpdateMessageRequest;
import project.school.socialmedia.dto.response.message.MessageResponse;
import project.school.socialmedia.dto.response.message.SimpleMessageResponse;
import project.school.socialmedia.service.MessageService;

@RestController
@RequestMapping("/messageApi")
@AllArgsConstructor
public class MessageController {

  private final MessageService messageService;

  @PatchMapping("/{messageId}")
  public ResponseEntity<SimpleMessageResponse> updateMessage(
          @PathVariable long messageId,
          @RequestBody UpdateMessageRequest updateMessageRequest
  ) {
    return ResponseEntity.status(HttpStatus.OK).body(
            messageService.update(messageId, updateMessageRequest)
    );
  }

  @PostMapping("/new")
  public ResponseEntity<MessageResponse> createMessage(
          @RequestBody CreateMessageRequest createMessageRequest
  ) throws JsonProcessingException {

    return ResponseEntity.status(HttpStatus.CREATED).body(
            messageService.create(createMessageRequest)
    );
  }

  @DeleteMapping("/{messageId}")
  public ResponseEntity<String> deleteMessage(
          @PathVariable long messageId
  ) {
    return ResponseEntity.status(HttpStatus.NO_CONTENT).body(
            messageService.delete(messageId)
    );
  }

  @PostMapping("/{conversationId}")
  public ResponseEntity<Page<MessageResponse>> getConversationMessages(
          @PathVariable long conversationId,
          @RequestBody GetMessagesRequest getMessagesRequest
  ) {
    Pageable pageable = PageRequest.of(getMessagesRequest.getPageNumber(), getMessagesRequest.getPageSize());
    Page<MessageResponse> messageResponsePage = messageService.get(conversationId, pageable);
    return ResponseEntity.status(200).body(messageResponsePage);
  }
}
