package org.example.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateReviewDto {
    private Long requestId;
    private Long studentId;
    private Short rating;
    private String comment;
}