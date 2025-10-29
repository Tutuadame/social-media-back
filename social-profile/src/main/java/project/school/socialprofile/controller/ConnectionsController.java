package project.school.socialprofile.controller;

import lombok.AllArgsConstructor;
import project.school.socialprofile.dto.request.connections.CheckConnectionsBatchRequest;
import project.school.socialprofile.dto.request.connections.CreateConnectionRequest;
import project.school.socialprofile.dto.request.connections.UpdateConnectionsStatusRequest;
import project.school.socialprofile.dto.response.connection.CheckConnectionsBatchResponse;
import project.school.socialprofile.dto.response.connection.ConnectionResponse;
import project.school.socialprofile.dto.response.connection.UpdateConnectionStatusResponse;
import project.school.socialprofile.service.ConnectionService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/connectionApi")
@AllArgsConstructor
public class ConnectionsController {
	private final ConnectionService connectionService;

	@GetMapping("/connections/accepted/{profileId}")
	public ResponseEntity<List<ConnectionResponse>> getAcceptedConnectionsByUser(
    @PathVariable(name = "profileId") String profileId
	){
    List<ConnectionResponse> connectionResponses = connectionService.getAcceptedConnections(profileId);
    return ResponseEntity.status(HttpStatus.OK).body(connectionResponses);
	}

	@GetMapping("/connections/pending")
	public ResponseEntity<Page<ConnectionResponse>> getPendingConnectionsByUser(
        @RequestParam(name = "profileId") String profileId,
        @RequestParam(name = "pageNumber") int pageNumber,
        @RequestParam(name = "pageSize") int pageSize
	){
		Pageable pageable = PageRequest.of(pageNumber, pageSize);
		Page<ConnectionResponse> connectionResponses = connectionService.getPendingConnections(profileId, pageable);
		return ResponseEntity.status(HttpStatus.OK).body(connectionResponses);
	}

	@PatchMapping("/update")
	public ResponseEntity<UpdateConnectionStatusResponse> updateConnection(
        @RequestBody(required = true) UpdateConnectionsStatusRequest updateConnectionsStatusRequest
	){
    UpdateConnectionStatusResponse updateConnectionStatusResponse =
            connectionService.updateConnectionStatus(updateConnectionsStatusRequest);
	return ResponseEntity.status(HttpStatus.OK).body(updateConnectionStatusResponse);
	}

	@PostMapping("/new")
	public ResponseEntity<ConnectionResponse> createConnection(
	@RequestBody(required = true) CreateConnectionRequest createConnectionRequest) {
		ConnectionResponse connectionResponses = connectionService.createConnection(createConnectionRequest);
		return ResponseEntity.status(HttpStatus.OK).body(connectionResponses);
	}

	@PostMapping("/checkConnectionsBatch")
	public ResponseEntity<CheckConnectionsBatchResponse> checkConnectionsBatch(
        @RequestBody(required = true) CheckConnectionsBatchRequest request
	) {
    List<String> connectedIds = connectionService.findConnectedIds(
        request.getRequesterId(), request.getTargetIds()
    );
		return ResponseEntity.ok(new CheckConnectionsBatchResponse(connectedIds));
	}

	@GetMapping("/checkConnection")
	public ResponseEntity<String> checkConnectionStatus(
        @RequestParam(name = "currentUserId") String currentUserId,
        @RequestParam(name = "targetUserId") String targetUserId
	){
		return ResponseEntity.status(HttpStatus.OK).body(connectionService.checkConnection(currentUserId, targetUserId));
	}

}
