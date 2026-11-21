package org.example.service;

import org.example.dto.CreateRequestDto;
import org.example.models.Request;
import org.example.models.enums.RequestStatus;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface RequestService {
    void createRequest(CreateRequestDto createRequestDto) throws SQLException;

    List<Request> getIncomingRequests(Long userId);

    List<Request> getOutgoingRequests(Long userId);

    void changeStatus(Long requestId, RequestStatus status);

    Optional<Request> getRequestById(Long id);
}
