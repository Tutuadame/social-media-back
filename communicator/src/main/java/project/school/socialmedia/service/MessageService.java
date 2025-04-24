package project.school.socialmedia.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import project.school.socialmedia.dto.request.message.CreateMessageRequest;
import project.school.socialmedia.dto.request.message.UpdateMessageRequest;
import project.school.socialmedia.dto.response.message.MessageResponse;
import project.school.socialmedia.dto.response.message.SimpleMessageResponse;

import java.util.NoSuchElementException;

public interface MessageService {

  @Transactional(readOnly = true)
  public Page<MessageResponse> get(long conversationId, Pageable pageable);

  @Transactional
  public String delete(long messageId) throws NoSuchElementException;

  @Transactional
  public SimpleMessageResponse update(long messageId, UpdateMessageRequest updateMessageRequest) throws NoSuchElementException;

  @Transactional
  public MessageResponse create(CreateMessageRequest request) throws NoSuchElementException, JsonProcessingException;
}
