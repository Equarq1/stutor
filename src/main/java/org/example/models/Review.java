package org.example.models;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Review {
    private Long id;
    private Long requestId;
    private User tutor;
    private User student;
    private Short rating;
    private String comment;
    private LocalDateTime createdAt;
}