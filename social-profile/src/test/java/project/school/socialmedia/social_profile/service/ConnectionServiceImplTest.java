package project.school.socialmedia.social_profile.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import project.school.socialprofile.config.KafkaConfigProps;
import project.school.socialprofile.domain.Connection;
import project.school.socialprofile.domain.ConnectionStatusEnum;
import project.school.socialprofile.domain.Profile;
import project.school.socialprofile.dto.kafka.Notification;
import project.school.socialprofile.dto.request.connections.CreateConnectionRequest;
import project.school.socialprofile.dto.request.connections.UpdateConnectionsStatusRequest;
import project.school.socialprofile.dto.response.connection.ConnectionResponse;
import project.school.socialprofile.dto.response.connection.UpdateConnectionStatusResponse;
import project.school.socialprofile.repository.ConnectionRepository;
import project.school.socialprofile.repository.ProfileRepository;
import project.school.socialprofile.service.impl.ConnectionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class ConnectionServiceImplTest {

  @Mock
  private ConnectionRepository connectionRepository;

  @Mock
  private ProfileRepository profileRepository;

  @Mock
  private KafkaTemplate<String, String> kafkaTemplate;

  @Mock
  private KafkaConfigProps kafkaConfigProps;

  @Mock
  private ObjectMapper objectMapper;

  @InjectMocks
  private ConnectionServiceImpl connectionService;

  private Profile initiatorProfile;
  private Profile targetProfile;
  private Connection testConnection;
  private final Pageable pageable = PageRequest.of(0, 10);

  @BeforeEach
  void setUp() throws JsonProcessingException {
    MockitoAnnotations.openMocks(this);

    // Setup test profiles
    initiatorProfile = new Profile("user123", "avatar1.jpg", null, "John", "Doe", "Initiator introduction");
    targetProfile = new Profile("user456", "avatar2.jpg", null, "Jane", "Smith", "Target introduction");

    // Setup test connection
    testConnection = new Connection(1L, initiatorProfile, targetProfile, ConnectionStatusEnum.PENDING);

    // Setup mock behavior for kafka
    when(kafkaConfigProps.getTopic()).thenReturn("notification.created");
    when(objectMapper.writeValueAsString(any(Notification.class))).thenReturn("serialized notification");
  }

  @Test
  void createConnection_ValidRequest_ReturnsConnectionResponse() {
    // Arrange
    CreateConnectionRequest request = new CreateConnectionRequest();
    request.setInitiatorId("user123");
    request.setTargetId("user456");

    when(profileRepository.findById("user123")).thenReturn(Optional.of(initiatorProfile));
    when(profileRepository.findById("user456")).thenReturn(Optional.of(targetProfile));
    when(connectionRepository.save(any(Connection.class))).thenReturn(testConnection);

    // Act
    ConnectionResponse result = connectionService.createConnection(request);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals("user456", result.getProfileId());
    assertEquals("Jane", result.getFirstName());
    assertEquals("Smith", result.getLastName());

    verify(profileRepository).findById("user123");
    verify(profileRepository).findById("user456");
    verify(connectionRepository).save(any(Connection.class));
    verify(kafkaTemplate).send(eq("notification.created"), anyString());
  }

  @Test
  void createConnection_InitiatorNotFound_ThrowsException() {
    // Arrange
    CreateConnectionRequest request = new CreateConnectionRequest();
    request.setInitiatorId("nonexistent");
    request.setTargetId("user456");

    when(profileRepository.findById("nonexistent")).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> connectionService.createConnection(request));
    verify(profileRepository).findById("nonexistent");
    verify(connectionRepository, never()).save(any(Connection.class));
  }

  @Test
  void updateConnectionStatus_ToAccepted_ReturnsUpdateResponse() {
    // Arrange
    UpdateConnectionsStatusRequest request = new UpdateConnectionsStatusRequest();
    request.setId(1L);
    request.setStatus("ACCEPTED");

    when(connectionRepository.findById(1L)).thenReturn(Optional.of(testConnection));
    when(connectionRepository.save(any(Connection.class))).thenReturn(testConnection);

    // Act
    UpdateConnectionStatusResponse result = connectionService.updateConnectionStatus(request);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals(ConnectionStatusEnum.ACCEPTED, testConnection.getStatus());

    verify(connectionRepository).findById(1L);
    verify(connectionRepository).save(testConnection);
  }

  @Test
  void updateConnectionStatus_ToBlocked_ReturnsUpdateResponse() {
    // Arrange
    UpdateConnectionsStatusRequest request = new UpdateConnectionsStatusRequest();
    request.setId(1L);
    request.setStatus("BLOCKED");

    when(connectionRepository.findById(1L)).thenReturn(Optional.of(testConnection));
    when(connectionRepository.save(any(Connection.class))).thenReturn(testConnection);

    // Act
    UpdateConnectionStatusResponse result = connectionService.updateConnectionStatus(request);

    // Assert
    assertNotNull(result);
    assertEquals(1L, result.getId());
    assertEquals(ConnectionStatusEnum.BLOCKED, testConnection.getStatus());

    verify(connectionRepository).findById(1L);
    verify(connectionRepository).save(testConnection);
  }

  @Test
  void updateConnectionStatus_ConnectionNotFound_ThrowsException() {
    // Arrange
    UpdateConnectionsStatusRequest request = new UpdateConnectionsStatusRequest();
    request.setId(999L);
    request.setStatus("ACCEPTED");

    when(connectionRepository.findById(999L)).thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () -> connectionService.updateConnectionStatus(request));
    verify(connectionRepository).findById(999L);
    verify(connectionRepository, never()).save(any(Connection.class));
  }

  @Test
  void getAcceptedConnections_ReturnsConnectionResponses() {
    // Arrange
    String profileId = "user123";
    Connection connection1 = new Connection(1L, initiatorProfile, targetProfile, ConnectionStatusEnum.ACCEPTED);

    Profile otherProfile = new Profile("user789", "avatar3.jpg", null, "Bob", "Johnson", "Other introduction");
    Connection connection2 = new Connection(2L, initiatorProfile, otherProfile, ConnectionStatusEnum.ACCEPTED);

    List<Connection> connections = Arrays.asList(connection1, connection2);

    when(connectionRepository.findAcceptedByProfileId(profileId)).thenReturn(connections);

    // Act
    List<ConnectionResponse> results = connectionService.getAcceptedConnections(profileId);

    // Assert
    assertNotNull(results);
    assertEquals(2, results.size());
    assertEquals("user456", results.getFirst().getProfileId());
    assertEquals("Jane", results.get(0).getFirstName());
    assertEquals("Smith", results.get(0).getLastName());
    assertEquals("user789", results.get(1).getProfileId());
    assertEquals("Bob", results.get(1).getFirstName());
    assertEquals("Johnson", results.get(1).getLastName());

    verify(connectionRepository).findAcceptedByProfileId(profileId);
  }

  @Test
  void findConnectedIds_ReturnsMatchingIds() {
    // Arrange
    String requesterId = "user123";
    List<String> targetIds = Arrays.asList("user456", "user789", "user101");
    List<String> connectedIds = Arrays.asList("user456", "user789");

    when(connectionRepository.findConnectedIds(requesterId, targetIds)).thenReturn(connectedIds);

    // Act
    List<String> results = connectionService.findConnectedIds(requesterId, targetIds);

    // Assert
    assertNotNull(results);
    assertEquals(2, results.size());
    assertTrue(results.contains("user456"));
    assertTrue(results.contains("user789"));

    verify(connectionRepository).findConnectedIds(requesterId, targetIds);
  }

  @Test
  void getPendingConnections_ReturnsPendingConnectionPage() {
    // Arrange
    String profileId = "user456"; // Target user
    Connection pendingConnection = new Connection(1L, initiatorProfile, targetProfile, ConnectionStatusEnum.PENDING);
    List<Connection> connections = Collections.singletonList(pendingConnection);
    Page<Connection> connectionPage = new PageImpl<>(connections);

    when(connectionRepository.findPendingByTargetId(eq(profileId), any(Pageable.class))).thenReturn(connectionPage);

    // Act
    Page<ConnectionResponse> results = connectionService.getPendingConnections(profileId, pageable);

    // Assert
    assertNotNull(results);
    assertEquals(1, results.getContent().size());
    assertEquals(1L, results.getContent().getFirst().getId());
    assertEquals("user123", results.getContent().getFirst().getProfileId()); // Initiator profile
    assertEquals("John", results.getContent().getFirst().getFirstName());
    assertEquals("Doe", results.getContent().getFirst().getLastName());

    verify(connectionRepository).findPendingByTargetId(eq(profileId), any(Pageable.class));
  }

  @Test
  void checkConnection_ReturnsConnectionStatus() {
    // Arrange
    String currentUserId = "user123";
    String targetUserId = "user456";

    when(connectionRepository.getConnectionStatus(currentUserId, targetUserId))
            .thenReturn(Optional.of(testConnection));

    // Act
    String result = connectionService.checkConnection(currentUserId, targetUserId);

    // Assert
    assertEquals("PENDING", result);
    verify(connectionRepository).getConnectionStatus(currentUserId, targetUserId);
  }

  @Test
  void checkConnection_NoConnection_ThrowsException() {
    // Arrange
    String currentUserId = "user123";
    String targetUserId = "nonexistent";

    when(connectionRepository.getConnectionStatus(currentUserId, targetUserId))
            .thenReturn(Optional.empty());

    // Act & Assert
    assertThrows(NoSuchElementException.class, () ->
            connectionService.checkConnection(currentUserId, targetUserId));
    verify(connectionRepository).getConnectionStatus(currentUserId, targetUserId);
  }
}