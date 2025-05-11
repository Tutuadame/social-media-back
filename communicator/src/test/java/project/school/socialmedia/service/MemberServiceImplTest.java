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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import project.school.socialmedia.domain.Conversation;
import project.school.socialmedia.domain.Member;
import project.school.socialmedia.domain.MemberConversations;
import project.school.socialmedia.dto.request.member.AddMemberRequest;
import project.school.socialmedia.dto.request.member.CreateMemberRequest;
import project.school.socialmedia.dto.response.member.CheckConnectionsBatchResponse;
import project.school.socialmedia.dto.response.member.MemberResponse;
import project.school.socialmedia.repository.ConversationRepository;
import project.school.socialmedia.repository.MemberConversationsRepository;
import project.school.socialmedia.repository.MemberRepository;
import project.school.socialmedia.service.impl.MemberServiceImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MemberServiceImplTest {

  @Mock
  private MemberRepository memberRepository;

  @Mock
  private MemberConversationsRepository memberConversationsRepository;

  @Mock
  private ConversationRepository conversationRepository;

  @Mock
  private RestTemplate restTemplate;

  @InjectMocks
  private MemberServiceImpl memberService;

  private Member testMember;
  private Conversation testConversation;
  private final Pageable pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Setup test member
    testMember = new Member("member1", "John", "Doe", "picture.jpg");

    // Setup test conversation
    testConversation = new Conversation();
    testConversation.setId(1L);
    testConversation.setName("Test Conversation");
  }

  @Test
  void create_ValidRequest_ReturnsNewMember() {
    // Arrange
    CreateMemberRequest request = new CreateMemberRequest();
    request.setMemberId("member1");
    request.setFirstName("John");
    request.setLastName("Doe");

    Member newMember = new Member("member1", "John", "Doe",
            "https://avatar.iran.liara.run/username?username=John+Doe");

    when(memberRepository.save(any(Member.class))).thenReturn(newMember);

    // Act
    Member result = memberService.create(request);

    // Assert
    assertNotNull(result);
    assertEquals("member1", result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertTrue(result.getPicture().contains("username=John+Doe"));
    verify(memberRepository).save(any(Member.class));
  }

  @Test
  void add_ValidRequest_ReturnsNewMemberResponse() {
    // Arrange
    AddMemberRequest request = new AddMemberRequest();
    request.setMemberId("member1");
    request.setConversationId(1L);

    when(memberRepository.findById("member1")).thenReturn(Optional.of(testMember));
    when(conversationRepository.findById(1L)).thenReturn(Optional.of(testConversation));
    when(memberConversationsRepository.save(any(MemberConversations.class)))
            .thenReturn(new MemberConversations(testMember, testConversation));

    // Act
    MemberResponse result = memberService.add(request);

    // Assert
    assertNotNull(result);
    assertEquals("member1", result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    assertEquals("picture.jpg", result.getPicture());
    verify(memberRepository).findById("member1");
    verify(conversationRepository).findById(1L);
    verify(memberConversationsRepository).save(any(MemberConversations.class));
  }

  @Test
  void add_NonExistingMember_ThrowsException() {
    // Arrange
    AddMemberRequest request = new AddMemberRequest();
    request.setMemberId("nonExistingMember");
    request.setConversationId(1L);

    when(memberRepository.findById("nonExistingMember")).thenReturn(Optional.empty());

    // Act & Assert
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      memberService.add(request);
    });
    assertEquals("Member not found!", exception.getMessage());
    verify(memberRepository).findById("nonExistingMember");
    verify(conversationRepository, never()).findById(anyLong());
  }

  @Test
  void add_NonExistingConversation_ThrowsException() {
    // Arrange
    AddMemberRequest request = new AddMemberRequest();
    request.setMemberId("member1");
    request.setConversationId(999L);

    when(memberRepository.findById("member1")).thenReturn(Optional.of(testMember));
    when(conversationRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      memberService.add(request);
    });
    assertEquals("Conversation not found!", exception.getMessage());
    verify(memberRepository).findById("member1");
    verify(conversationRepository).findById(999L);
  }

  @Test
  void delete_ExistingMember_ReturnsSuccessMessage() {
    // Arrange
    String memberId = "member1";
    when(memberRepository.findById(memberId)).thenReturn(Optional.of(testMember));
    doNothing().when(memberRepository).delete(testMember);
    doNothing().when(memberConversationsRepository).deleteByMember_Id(memberId);

    // Act
    String result = memberService.delete(memberId);

    // Assert
    assertEquals("Delete was successful!", result);
    verify(memberRepository).findById(memberId);
    verify(memberRepository).delete(testMember);
    verify(memberConversationsRepository).deleteByMember_Id(memberId);
  }

  @Test
  void delete_NonExistingMember_ThrowsException() {
    // Arrange
    String memberId = "nonExistingMember";
    when(memberRepository.findById(memberId)).thenReturn(Optional.empty());

    // Act & Assert
    NoSuchElementException exception = assertThrows(NoSuchElementException.class, () -> {
      memberService.delete(memberId);
    });
    assertEquals("Member not found!", exception.getMessage());
    verify(memberRepository).findById(memberId);
    verify(memberRepository, never()).delete(any(Member.class));
  }

  @Test
  void deleteFromConversation_ExistingMemberAndConversation_ReturnsSuccessMessage() {
    // Arrange
    String memberId = "member1";
    Long conversationId = 1L;

    doNothing().when(memberConversationsRepository)
            .deleteByMember_IdAndConversation_Id(memberId, conversationId);
    when(memberConversationsRepository.findByConversation_Id(conversationId))
            .thenReturn(Collections.emptyList());
    doNothing().when(conversationRepository).deleteById(conversationId);

    // Act
    String result = memberService.deleteFromConversation(memberId, conversationId);

    // Assert
    assertEquals("Delete was successful!", result);
    verify(memberConversationsRepository).deleteByMember_IdAndConversation_Id(memberId, conversationId);
    verify(memberConversationsRepository).findByConversation_Id(conversationId);
    verify(conversationRepository).deleteById(conversationId);
  }

  @Test
  void getConversationMembers_ExistingMembers_ReturnsMemberResponses() {
    // Arrange
    List<String> memberIds = Arrays.asList("member1", "member2");
    Member member2 = new Member("member2", "Jane", "Smith", "picture2.jpg");
    List<Member> members = Arrays.asList(testMember, member2);

    when(memberRepository.findAllById(memberIds)).thenReturn(members);

    // Act
    List<MemberResponse> result = memberService.getConversationMembers(memberIds);

    // Assert
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("member1", result.get(0).getId());
    assertEquals("member2", result.get(1).getId());
    verify(memberRepository).findAllById(memberIds);
  }

  @Test
  void searchForMembersByName_WithConnections_ReturnsFilteredMembers() {
    // Arrange
    String name = "John";
    String requesterId = "requester1";
    String accessToken = "token123";

    Member member1 = new Member("member1", "John", "Doe", "picture1.jpg");
    Member member2 = new Member("member2", "John", "Smith", "picture2.jpg");
    List<Member> members = Arrays.asList(member1, member2);
    Page<Member> memberPage = new PageImpl<>(members);

    List<String> connectedIds = Collections.singletonList("member1");
    CheckConnectionsBatchResponse checkResponse = new CheckConnectionsBatchResponse();
    checkResponse.setConnectedIds(connectedIds);

    when(memberRepository.searchByName(eq(name), any(Pageable.class))).thenReturn(memberPage);

    ResponseEntity<CheckConnectionsBatchResponse> responseEntity =
            ResponseEntity.ok(checkResponse);

    when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(CheckConnectionsBatchResponse.class)))
            .thenReturn(responseEntity);

    // Act
    Page<MemberResponse> result = memberService.searchForMembersByName(name, requesterId, pageable, accessToken);

    // Assert
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("member1", result.getContent().getFirst().getId());
    verify(memberRepository).searchByName(eq(name), any(Pageable.class));
    verify(restTemplate).exchange(
            anyString(),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(CheckConnectionsBatchResponse.class));
  }

  @Test
  void searchForMembersByName_ApiError_ReturnsEmptyList() {
    // Arrange
    String name = "John";
    String requesterId = "requester1";
    String accessToken = "token123";

    Member member1 = new Member("member1", "John", "Doe", "picture1.jpg");
    Member member2 = new Member("member2", "John", "Smith", "picture2.jpg");
    List<Member> members = Arrays.asList(member1, member2);
    Page<Member> memberPage = new PageImpl<>(members);

    when(memberRepository.searchByName(eq(name), any(Pageable.class))).thenReturn(memberPage);
    when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(CheckConnectionsBatchResponse.class)))
            .thenThrow(new RuntimeException("API Error"));

    // Act
    Page<MemberResponse> result = memberService.searchForMembersByName(name, requesterId, pageable, accessToken);

    // Assert
    assertNotNull(result);
    assertEquals(0, result.getContent().size());
    verify(memberRepository).searchByName(eq(name), any(Pageable.class));
    verify(restTemplate).exchange(
            anyString(),
            eq(HttpMethod.POST),
            any(HttpEntity.class),
            eq(CheckConnectionsBatchResponse.class));
  }
}