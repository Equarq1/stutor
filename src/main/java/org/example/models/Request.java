package org.example.models;

import lombok.Builder;
import lombok.Data;
import org.example.models.enums.RequestStatus;

import java.time.LocalDateTime;

@Data
@Builder
public class Request {
    private Long id;
    private Offer offer;
    private Subject subject;
    private String message;
    private LocalDateTime createdAt;
    private RequestStatus status;
    private User student;
    private FileInfo attachment;
}
