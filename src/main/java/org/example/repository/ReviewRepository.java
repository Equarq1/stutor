package org.example.repository;

import org.example.models.Review;

import java.util.List;

public interface ReviewRepository extends CrudRepository<Review> {
    List<Review> findByTutorId(Long tutorId);
    boolean existsByRequestId(Long requestId);
    Double getAverageRatingByTutorId(Long tutorId);
}