package project.school.socialmedia.service;

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
import project.school.socialmedia.domain.Conversation;
import project.school.socialmedia.domain.Member;
import project.school.socialmedia.domain.MemberConversations;
import project.school.socialmedia.dto.request.conversation.CreateConversationRequest;
import project.school.socialmedia.dto.request.conversation.UpdateNamingRequest;
import project.school.socialmedia.dto.response.conversation.ConversationResponse;
import project.school.socialmedia.dto.response.conversation.SimpleConversationResponse;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;
import project.school.socialmedia.service.impl.ConversationServiceImpl;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConversationServiceImplTest {

  @Mock
  private ConversationRepository conversationRepository;

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private MemberConversationsRepository memberConversationsRepository;

  @InjectMocks
  private ConversationServiceImpl conversationService;

  private Conversation testConversation;
  private Member testMember1, testMember2;
  private List<MemberConversations> testMemberConversations;
  private final Pageable pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Setup test conversation
    testConversation = new Conversation();
    testConversation.setId(1L);
    testConversation.setName("Test Conversation");

    // Setup test members
    testMember1 = new Member("member1", "John", "Doe", "picture1.jpg");
    testMember2 = new Member("member2", "Jane", "Smith", "picture2.jpg");

    // Setup MemberConversations
    MemberConversations mc1 = new MemberConversations(1L, testMember1, testConversation);
    MemberConversations mc2 = new MemberConversations(2L, testMember2, testConversation);
    testMemberConversations = Arrays.asList(mc1, mc2);

    testConversation.setMemberConversations(testMemberConversations);
  }

  @Test
  void get_ExistingConversation_ReturnsConversation() {
    // Arrange
    when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));

    // Act
    Conversation result = conversationService.get(1L);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Test Conversation", result.getName());
    verify(conversationRepository).findById(1L);
  }

  @Test
  void get_NonExistingConversation_ThrowsException() {
    // Arrange
    when(conversationRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      conversationService.get(999L);
    });
    assertEquals("Conversation not found!", exception.getMessage());
    verify(conversationRepository).findById(999L);
  }

  @Test
  void delete_ExistingConversation_ReturnsSuccessMessage() {
    // Arrange
    when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
    doNothing().when(conversationRepository).delete(testConversation);

    // Act
    String result = conversationService.delete(1L);

    // Assert
    assertEquals("Conversation was deleted", result);
    verify(conversationRepository).findById(1L);
    verify(conversationRepository).delete(testConversation);
  }

  @Test
  void update_ExistingConversation_ReturnsUpdatedConversation() {
    // Arrange
    UpdateNamingRequest request = new UpdateNamingRequest();
    request.setName("Updated Conversation");

    Conversation updatedConversation = new Conversation();
    updatedConversation.setId(1L);
    updatedConversation.setName("Updated Conversation");

    when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
    when(conversationRepository.save(any(Conversation.class))).thenReturn(updatedConversation);

    // Act
    Conversation result = conversationService.update(1L, request);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("Updated Conversation", result.getName());
    verify(conversationRepository).findById(1L);
    verify(conversationRepository).save(any(Conversation.class));
  }

  @Test
  void create_ValidRequest_ReturnsNewConversation() throws SQLIntegrityConstraintViolationException {
    // Arrange
    CreateConversationRequest request = new CreateConversationRequest();
    request.setName("New Conversation");
    request.setMembers(new String[]{"member1", "member2"});

    Conversation newConversation = new Conversation("New Conversation");
    newConversation.setId(1L);

    when(memberRepository.findById("member1")).thenReturn(Optional.of(testMember1));
    when(memberRepository.findById("member2")).thenReturn(Optional.of(testMember2));
    when(conversationRepository.save(any(Conversation.class))).thenReturn(newConversation);

    // Act
    Conversation result = conversationService.create(request);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("New Conversation", result.getName());
    verify(conversationRepository).save(any(Conversation.class));
    verify(memberRepository, times(2)).findById(anyString());
    verify(memberConversationsRepository).saveAll(anyList());
  }

  @Test
  void create_EmptyName_ThrowsException() {
    // Arrange
    CreateConversationRequest request = new CreateConversationRequest();
    request.setName("");
    request.setMembers(new String[]{"member1", "member2"});

    // Act & Assert
    assertThrows(SQLIntegrityConstraintViolationException.class, () -> {
      conversationService.create(request);
    });
    verify(conversationRepository, never()).save(any(Conversation.class));
  }

  @Test
  void create_NonExistingMember_ThrowsException() {
    // Arrange
    CreateConversationRequest request = new CreateConversationRequest();
    request.setName("New Conversation");
    request.setMembers(new String[]{"member1", "nonExistingMember"});

    Conversation newConversation = new Conversation("New Conversation");
    newConversation.setId(1L);

    when(memberRepository.findById("member1")).thenReturn(Optional.of(testMember1));
    when(memberRepository.findById("nonExistingMember")).thenReturn(Optional.empty());
    when(conversationRepository.save(any(Conversation.class))).thenReturn(newConversation);

    // Act & Assert
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      conversationService.create(request);
    });
    assertEquals("Wrong user! This user is not registered!", exception.getMessage());
    verify(conversationRepository).save(any(Conversation.class));
    verify(memberRepository, times(2)).findById(anyString());
  }

  @Test
  void getMemberConversations_ExistingMember_ReturnsConversationPage() {
    // Arrange
    List<MemberConversations> memberConversationsList = Collections.singletonList(
            new MemberConversations(1L, testMember1, testConversation)
    );
    Page<MemberConversations> memberConversationsPage = new PageImpl<>(memberConversationsList);

    when(memberConversationsRepository.findByMember_Id(eq("member1"), any(Pageable.class)))
            .thenReturn(memberConversationsPage);

    // Act
    Page<SimpleConversationResponse> result = conversationService.getMemberConversations("member1", pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(1L, result.getContent().getFirst().getId());
    assertEquals("Test Conversation", result.getContent().getFirst().getName());
    verify(memberConversationsRepository).findByMember_Id(eq("member1"), any(Pageable.class));
  }

  @Test
  void searchByName_ExistingName_ReturnsMatchingConversations() {
    // Arrange
    List<Conversation> conversations = Collections.singletonList(testConversation);
    Page<Conversation> conversationPage = new PageImpl<>(conversations);

    when(conversationRepository.searchByNameForMember(eq("Test"), eq("member1"), any(Pageable.class)))
            .thenReturn(conversationPage);

    // Act
    Page<SimpleConversationResponse> result = conversationService.searchByName("Test", "member1", pageable);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals(1L, result.getContent().getFirst().getId());
    assertEquals("Test Conversation", result.getContent().getFirst().getName());
    verify(conversationRepository).searchByNameForMember(eq("Test"), eq("member1"), any(Pageable.class));
  }

  @Test
  void getConversation_ExistingId_ReturnsConversationResponse() {
    // Arrange
    when(conversationRepository.findById(any())).thenReturn(Optional.of(testConversation));

    // Act
    ConversationResponse result = conversationService.getConversation("1");

    // Assert
    assertNotNull(result);
    assertEquals("1", result.getId());
    assertEquals("Test Conversation", result.getName());
    assertEquals(2, result.getMemberIds().size());
    assertTrue(result.getMemberIds().contains("member1"));
    assertTrue(result.getMemberIds().contains("member2"));
    verify(conversationRepository).findById(any());
  }

  @Test
  void getConversation_NonExistingId_ThrowsException() {
    // Arrange
    when(conversationRepository.findById(any())).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> {
      conversationService.getConversation("999");
    });
    verify(conversationRepository).findById(any());
  }
}