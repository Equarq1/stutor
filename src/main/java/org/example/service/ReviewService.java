package org.example.service;

import org.example.dto.CreateReviewDto;
import org.example.models.Review;

import java.sql.SQLException;
import java.util.List;

public interface ReviewService {
    Long createReview(CreateReviewDto dto) throws SQLException;
    List<Review> getReviewsByTutorId(Long tutorId);
    Double getAverageRatingByTutorId(Long tutorId);
    boolean existsByRequestId(Long requestId);
}