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
import project.school.socialmedia.domain.Member;
import project.school.socialmedia.dto.request.member.AddMemberRequest;
import project.school.socialmedia.dto.request.member.CreateMemberRequest;
import project.school.socialmedia.dto.request.member.DeleteMemberFromConversationRequest;
import project.school.socialmedia.dto.response.member.MemberResponse;
import project.school.socialmedia.service.impl.MemberServiceImpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MemberControllerTest {

  @Mock
  private MemberServiceImpl memberService;

  @InjectMocks
  private MemberController memberController;

  private Member testMember;
  private MemberResponse testMemberResponse;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Setup test member
    testMember = new Member("member1", "John", "Doe", "picture.jpg");

    // Setup test member response
    testMemberResponse = new MemberResponse("member1", "John", "Doe", "picture.jpg");
  }

  @Test
  void createMember_ReturnsCreatedMember() {
    // Arrange
    CreateMemberRequest request = new CreateMemberRequest();
    request.setMemberId("member1");
    request.setFirstName("John");
    request.setLastName("Doe");

    when(memberService.create(request)).thenReturn(testMember);

    // Act
    ResponseEntity<Member> response = memberController.createMember(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    Member result = response.getBody();
    assertNotNull(result);
    assertEquals("member1", result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    verify(memberService).create(request);
  }

  @Test
  void deleteMember_ReturnsSuccessMessage() {
    // Arrange
    String memberId = "member1";
    when(memberService.delete(memberId)).thenReturn("Delete was successful!");

    // Act
    ResponseEntity<String> response = memberController.deleteMember(memberId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Delete was successful!", response.getBody());
    verify(memberService).delete(memberId);
  }

  @Test
  void addMember_ReturnsAddedMember() {
    // Arrange
    AddMemberRequest request = new AddMemberRequest();
    request.setMemberId("member1");
    request.setConversationId(1L);

    when(memberService.add(request)).thenReturn(testMemberResponse);

    // Act
    ResponseEntity<MemberResponse> response = memberController.addMember(request);

    // Assert
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    MemberResponse result = response.getBody();
    assertNotNull(result);
    assertEquals("member1", result.getId());
    assertEquals("John", result.getFirstName());
    assertEquals("Doe", result.getLastName());
    verify(memberService).add(request);
  }

  @Test
  void deleteMemberFromConversation_ReturnsSuccessMessage() {
    // Arrange
    DeleteMemberFromConversationRequest request = new DeleteMemberFromConversationRequest();
    request.setMemberId("member1");
    request.setConversationId(1L);

    when(memberService.deleteFromConversation("member1", 1L)).thenReturn("Delete was successful!");

    // Act
    ResponseEntity<String> response = memberController.deleteMemberFromConversation(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Delete was successful!", response.getBody());
    verify(memberService).deleteFromConversation("member1", 1L);
  }

  @Test
  void searchForMembers_ReturnsMatchingMembers() {
    // Arrange
    String name = "John";
    String requesterId = "requester1";
    int pageSize = 10;
    int pageNumber = 0;
    String authHeader = "Bearer token123";

    List<MemberResponse> members = new ArrayList<>();
    members.add(testMemberResponse);
    Page<MemberResponse> memberPage = new PageImpl<>(members);

    when(memberService.searchForMembersByName(eq(name), eq(requesterId), any(Pageable.class), eq("token123")))
            .thenReturn(memberPage);

    // Act
    ResponseEntity<Page<MemberResponse>> response =
            memberController.searchForMembers(name, requesterId, pageSize, pageNumber, authHeader);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<MemberResponse> result = response.getBody();
    assertNotNull(result);
    assertEquals(1, result.getContent().size());
    assertEquals("member1", result.getContent().getFirst().getId());
    verify(memberService).searchForMembersByName(eq(name), eq(requesterId), any(Pageable.class), eq("token123"));
  }

  @Test
  void searchForMembers_NoAuthHeader_PassesNullToken() {
    // Arrange
    String name = "John";
    String requesterId = "requester1";
    int pageSize = 10;
    int pageNumber = 0;

    List<MemberResponse> members = new ArrayList<>();
    members.add(testMemberResponse);
    Page<MemberResponse> memberPage = new PageImpl<>(members);

    when(memberService.searchForMembersByName(eq(name), eq(requesterId), any(Pageable.class), isNull()))
            .thenReturn(memberPage);

    // Act
    ResponseEntity<Page<MemberResponse>> response =
            memberController.searchForMembers(name, requesterId, pageSize, pageNumber, null);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    verify(memberService).searchForMembersByName(eq(name), eq(requesterId), any(Pageable.class), isNull());
  }

  @Test
  void getMembers_ReturnsRequestedMembers() {
    // Arrange
    List<String> memberIds = Arrays.asList("member1", "member2");
    List<MemberResponse> memberResponses = Arrays.asList(
            testMemberResponse,
            new MemberResponse("member2", "Jane", "Smith", "picture2.jpg")
    );

    when(memberService.getConversationMembers(memberIds)).thenReturn(memberResponses);

    // Act
    ResponseEntity<List<MemberResponse>> response = memberController.getMembers(memberIds);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<MemberResponse> result = response.getBody();
    assertNotNull(result);
    assertEquals(2, result.size());
    assertEquals("member1", result.get(0).getId());
    assertEquals("member2", result.get(1).getId());
    verify(memberService).getConversationMembers(memberIds);
  }
}
