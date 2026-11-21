package org.example.dto;

import lombok.Builder;
import lombok.Data;
import org.example.models.FileInfo;
import org.example.models.Offer;
import org.example.models.Subject;
import org.example.models.User;
import org.example.models.enums.RequestStatus;

import java.time.LocalDateTime;

@Builder
@Data
public class CreateRequestDto {
    private Long offerId;
    private Integer subjectId;
    private String message;
    private Long studentId;
    private Long attachmentId;
}
