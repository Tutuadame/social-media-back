package project.school.socialmedia.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import project.school.socialmedia.dto.request.connections.CheckConnectionsBatchRequest;
import project.school.socialmedia.dto.request.connections.CreateConnectionRequest;
import project.school.socialmedia.dto.request.connections.GetPageablePendingConnectionsRequest;
import project.school.socialmedia.dto.request.connections.UpdateConnectionsStatusRequest;
import project.school.socialmedia.dto.response.connection.CheckConnectionsBatchResponse;
import project.school.socialmedia.dto.response.connection.ConnectionResponse;
import project.school.socialmedia.dto.response.connection.UpdateConnectionStatusResponse;
import project.school.socialmedia.service.ConnectionService;

import java.util.List;

@RestController
@RequestMapping("/connectionApi")
@AllArgsConstructor
public class ConnectionsController {
  private final ConnectionService connectionService;

  @PostMapping("/connections/accepted/{profileId}")
  public ResponseEntity<List<ConnectionResponse>> getAcceptedConnectionsByUser(
          @PathVariable String profileId
  ){
    List<ConnectionResponse> connectionResponses = connectionService.getAcceptedConnections(profileId);
    return ResponseEntity.status(HttpStatus.OK).body(connectionResponses);
  }

  @PostMapping("/connections/pending/{profileId}")
  public ResponseEntity<Page<ConnectionResponse>> getPendingConnectionsByUser(
          @PathVariable String profileId,
          @RequestBody  GetPageablePendingConnectionsRequest getPageablePendingConnectionsRequest
  ){
    Pageable pageable = PageRequest.of(getPageablePendingConnectionsRequest.getPageNumber(), getPageablePendingConnectionsRequest.getPageSize());
    Page<ConnectionResponse> connectionResponses = connectionService.getPendingConnections(profileId, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(connectionResponses);
  }

  @PatchMapping("/update")
  public ResponseEntity<UpdateConnectionStatusResponse> updateConnection(
          @RequestBody UpdateConnectionsStatusRequest updateConnectionsStatusRequest
  ){
    UpdateConnectionStatusResponse updateConnectionStatusResponse =
            connectionService.updateConnectionStatus(updateConnectionsStatusRequest);
    return ResponseEntity.status(HttpStatus.OK).body(updateConnectionStatusResponse);
  }

  @PostMapping("/new")
  public ResponseEntity<ConnectionResponse> createConnection(
          @RequestBody CreateConnectionRequest createConnectionRequest
  ) {
    ConnectionResponse connectionResponses = connectionService.createConnection(createConnectionRequest);
    return ResponseEntity.status(HttpStatus.OK).body(connectionResponses);
  }

  @PostMapping("/checkConnectionsBatch")
  public ResponseEntity<CheckConnectionsBatchResponse> checkConnectionsBatch(
          @RequestBody CheckConnectionsBatchRequest request
  ) {

    List<String> connectedIds = connectionService.findConnectedIds(
            request.getRequesterId(), request.getTargetIds()
    );
    return ResponseEntity.ok(new CheckConnectionsBatchResponse(connectedIds));
  }

  @GetMapping("/checkConnection")
  public ResponseEntity<String> checkConnectionStatus(
          @RequestParam String currentUserId,
          @RequestParam String targetUserId
  ){
    return ResponseEntity.status(HttpStatus.OK).body(connectionService.checkConnection(currentUserId, targetUserId));
  }

}
