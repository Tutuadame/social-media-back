package project.school.socialmedia.social_profile.controller;

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

import project.school.socialprofile.controller.ConnectionsController;
import project.school.socialprofile.dto.request.connections.CheckConnectionsBatchRequest;
import project.school.socialprofile.dto.request.connections.CreateConnectionRequest;
import project.school.socialprofile.dto.request.connections.UpdateConnectionsStatusRequest;
import project.school.socialprofile.dto.response.connection.CheckConnectionsBatchResponse;
import project.school.socialprofile.dto.response.connection.ConnectionResponse;
import project.school.socialprofile.dto.response.connection.UpdateConnectionStatusResponse;
import project.school.socialprofile.service.ConnectionService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConnectionsControllerTest {

  @Mock
  private ConnectionService connectionService;

  @InjectMocks
  private ConnectionsController connectionsController;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void getAcceptedConnectionsByUser_ReturnsConnectionsList() {
    // Arrange
    String profileId = "user123";
    List<ConnectionResponse> expectedConnections = Arrays.asList(
            new ConnectionResponse(1L, "user456", "John", "Doe", "picture1.jpg"),
            new ConnectionResponse(2L, "user789", "Jane", "Smith", "picture2.jpg")
    );

    when(connectionService.getAcceptedConnections(profileId)).thenReturn(expectedConnections);

    // Act
    ResponseEntity<List<ConnectionResponse>> response = connectionsController.getAcceptedConnectionsByUser(profileId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<ConnectionResponse> actualConnections = response.getBody();
    assertNotNull(actualConnections);
    assertEquals(2, actualConnections.size());
    assertEquals("user456", actualConnections.get(0).getProfileId());
    assertEquals("user789", actualConnections.get(1).getProfileId());
    verify(connectionService).getAcceptedConnections(profileId);
  }

  @Test
  void getPendingConnectionsByUser_ReturnsConnectionsPage() {
    // Arrange
    String profileId = "user123";
    int pageSize = 10;
    int pageNumber = 0;

    List<ConnectionResponse> connections = List.of(
            new ConnectionResponse(1L, "user456", "John", "Doe", "picture1.jpg")
    );
    Page<ConnectionResponse> expectedPage = new PageImpl<>(connections);

    when(connectionService.getPendingConnections(eq(profileId), any(Pageable.class)))
            .thenReturn(expectedPage);

    // Act
    ResponseEntity<Page<ConnectionResponse>> response =
            connectionsController.getPendingConnectionsByUser(profileId, pageNumber, pageSize);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Page<ConnectionResponse> actualPage = response.getBody();
    assertNotNull(actualPage);
    assertEquals(1, actualPage.getContent().size());
    assertEquals("user456", actualPage.getContent().getFirst().getProfileId());
    verify(connectionService).getPendingConnections(eq(profileId), any(Pageable.class));
  }

  @Test
  void updateConnection_ReturnsUpdateResponse() {
    // Arrange
    UpdateConnectionsStatusRequest request = new UpdateConnectionsStatusRequest();
    request.setId(1L);
    request.setStatus("ACCEPTED");

    UpdateConnectionStatusResponse expectedResponse = new UpdateConnectionStatusResponse(1L);

    when(connectionService.updateConnectionStatus(request)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<UpdateConnectionStatusResponse> response = connectionsController.updateConnection(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    UpdateConnectionStatusResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals(1L, actualResponse.getId());
    verify(connectionService).updateConnectionStatus(request);
  }

  @Test
  void createConnection_ReturnsConnectionResponse() {
    // Arrange
    CreateConnectionRequest request = new CreateConnectionRequest();
    request.setInitiatorId("user123");
    request.setTargetId("user456");

    ConnectionResponse expectedResponse = new ConnectionResponse(1L, "user456", "John", "Doe", "picture.jpg");

    when(connectionService.createConnection(request)).thenReturn(expectedResponse);

    // Act
    ResponseEntity<ConnectionResponse> response = connectionsController.createConnection(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    ConnectionResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals(1L, actualResponse.getId());
    assertEquals("user456", actualResponse.getProfileId());
    verify(connectionService).createConnection(request);
  }

  @Test
  void checkConnectionsBatch_ReturnsConnectedIds() {
    // Arrange
    CheckConnectionsBatchRequest request = new CheckConnectionsBatchRequest();
    request.setRequesterId("user123");
    request.setTargetIds(Arrays.asList("user456", "user789", "user101"));

    List<String> connectedIds = Arrays.asList("user456", "user789");

    when(connectionService.findConnectedIds(request.getRequesterId(), request.getTargetIds()))
            .thenReturn(connectedIds);

    // Act
    ResponseEntity<CheckConnectionsBatchResponse> response =
            connectionsController.checkConnectionsBatch(request);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    CheckConnectionsBatchResponse actualResponse = response.getBody();
    assertNotNull(actualResponse);
    assertEquals(2, actualResponse.getConnectedIds().size());
    assertEquals("user456", actualResponse.getConnectedIds().get(0));
    assertEquals("user789", actualResponse.getConnectedIds().get(1));
    verify(connectionService).findConnectedIds(request.getRequesterId(), request.getTargetIds());
  }

  @Test
  void checkConnectionStatus_ReturnsConnectionStatus() {
    // Arrange
    String currentUserId = "user123";
    String targetUserId = "user456";
    String expectedStatus = "ACCEPTED";

    when(connectionService.checkConnection(currentUserId, targetUserId)).thenReturn(expectedStatus);

    // Act
    ResponseEntity<String> response = connectionsController.checkConnectionStatus(currentUserId, targetUserId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    String actualStatus = response.getBody();
    assertNotNull(actualStatus);
    assertEquals(expectedStatus, actualStatus);
    verify(connectionService).checkConnection(currentUserId, targetUserId);
  }

  @Test
  void getAcceptedConnectionsByUser_EmptyList_ReturnsEmptyResponse() {
    // Arrange
    String profileId = "user123";
    List<ConnectionResponse> emptyList = new ArrayList<>();

    when(connectionService.getAcceptedConnections(profileId)).thenReturn(emptyList);

    // Act
    ResponseEntity<List<ConnectionResponse>> response = connectionsController.getAcceptedConnectionsByUser(profileId);

    // Assert
    assertEquals(HttpStatus.OK, response.getStatusCode());
    List<ConnectionResponse> actualConnections = response.getBody();
    assertNotNull(actualConnections);
    assertEquals(0, actualConnections.size());
    verify(connectionService).getAcceptedConnections(profileId);
  }
}
