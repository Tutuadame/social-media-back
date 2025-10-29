package project.school.socialprofile.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import project.school.socialprofile.dto.request.connections.CreateConnectionRequest;
import project.school.socialprofile.dto.request.connections.UpdateConnectionsStatusRequest;
import project.school.socialprofile.dto.response.connection.ConnectionResponse;
import project.school.socialprofile.dto.response.connection.UpdateConnectionStatusResponse;

import java.util.List;

public interface ConnectionService {
  ConnectionResponse createConnection(CreateConnectionRequest createConnectionRequest);
  UpdateConnectionStatusResponse updateConnectionStatus(UpdateConnectionsStatusRequest updateConnectionsStatusRequest);
  List<ConnectionResponse> getAcceptedConnections(String profileId);
  List<String> findConnectedIds(String userId1, List<String> targetIds);
  Page<ConnectionResponse> getPendingConnections(String profileId, Pageable pageable);
  String checkConnection(String currentUserId, String targetUserId);
}