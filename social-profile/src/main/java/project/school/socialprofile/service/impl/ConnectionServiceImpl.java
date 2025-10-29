package project.school.socialprofile.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
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
import project.school.socialprofile.service.ConnectionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
@AllArgsConstructor
public class ConnectionServiceImpl implements ConnectionService {

  private final ConnectionRepository connectionRepository;

  private final ProfileRepository profileRepository;

  private final KafkaTemplate<String, String> kafkaTemplate;

  private final KafkaConfigProps kafkaConfigProps;

  private final ObjectMapper objectMapper;

  @Override
  public ConnectionResponse createConnection(CreateConnectionRequest createConnectionRequest) {
    Profile initiator = profileRepository.findById(createConnectionRequest.getInitiatorId()).orElseThrow(NoSuchElementException::new);
    Profile target = profileRepository.findById(createConnectionRequest.getTargetId()).orElseThrow(NoSuchElementException::new);
    Connection connection = new Connection(initiator, target, ConnectionStatusEnum.PENDING);
    connection = connectionRepository.save(connection);

    try {
      Notification notification = createNotification(initiator, target, LocalDateTime.now());
      sendNotificationWithKafka(notification);
    } catch (JsonProcessingException | NoSuchElementException e) {
      System.out.println(e.getMessage());
    }

    return new ConnectionResponse(
            connection.getId(),
            createConnectionRequest.getTargetId(),
            target.getFirstName(),
            target.getLastName(),
            target.getPicture()
    );
  }

  @Override
  public UpdateConnectionStatusResponse updateConnectionStatus(UpdateConnectionsStatusRequest updateConnectionsStatusRequest) {
    Connection connection = connectionRepository.findById(updateConnectionsStatusRequest.getId()).orElseThrow(NoSuchElementException::new);
    ConnectionStatusEnum connectionStatusEnum = checkConnectionStatus(updateConnectionsStatusRequest);
    connection.setStatus(connectionStatusEnum);
    connectionRepository.save(connection);
    return new UpdateConnectionStatusResponse(connection.getId());
  }

  @Override
  public List<ConnectionResponse> getAcceptedConnections(String profileId) {
    List<Connection> connections = connectionRepository.findAcceptedByProfileId(profileId);
      //We always need the other user from the connection
    return connections.stream().map((connection -> {
      Profile profile = profileId.equals(connection.getTarget().getId()) ? connection.getInitiator() : connection.getTarget();
      return new ConnectionResponse(
              connection.getId(),
              profile.getId(),
              profile.getFirstName(),
              profile.getLastName(),
              profile.getPicture()
      );
    })).toList();
  }

  public List<String> findConnectedIds(String requesterId, List<String> targetIds) {
    return connectionRepository.findConnectedIds(requesterId, targetIds);
  }

  @Override
  public Page<ConnectionResponse> getPendingConnections(String profileId, Pageable pageable) {
    Page<Connection> connections = connectionRepository.findPendingByTargetId(profileId, pageable);
    return connections.map((connection -> {
      Profile profile = profileId.equals(connection.getTarget().getId()) ? connection.getInitiator() : connection.getTarget();
      return new ConnectionResponse(
              connection.getId(),
              profile.getId(),
              profile.getFirstName(),
              profile.getLastName(),
              profile.getPicture()
      );
    }));
  }

  @Override
  public String checkConnection(String currentUserId, String targetUserId) {
    return connectionRepository.getConnectionStatus(currentUserId, targetUserId)
            .orElseThrow(NoSuchElementException::new).getStatus().toString();
  }

  private ConnectionStatusEnum checkConnectionStatus(UpdateConnectionsStatusRequest updateConnectionsStatusRequest) {
    return switch (updateConnectionsStatusRequest.getStatus()) {
      case "ACCEPTED" -> ConnectionStatusEnum.ACCEPTED;
      case "PENDING" -> ConnectionStatusEnum.PENDING;
      default -> ConnectionStatusEnum.BLOCKED;
    };
  }


  private Notification createNotification(Profile initiator, Profile target, LocalDateTime sentAt) {
    String notificationMessage = "You have a new connection request from " + initiator.getFirstName() + " " + initiator.getLastName();

    List<String> receiverIds = List.of(target.getId());

    return Notification.builder()
            .createdAt(sentAt)
            .userIds(receiverIds)
            .message(notificationMessage)
            .build();
  }

  private void sendNotificationWithKafka(Notification notification) throws JsonProcessingException {
    final String payload = objectMapper.writeValueAsString(notification);
    kafkaTemplate.send(kafkaConfigProps.getTopic(), payload);
  }
}
