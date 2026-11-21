package org.example.service;

import org.example.dto.CreateReviewDto;
import org.example.models.Request;
import org.example.models.Review;
import org.example.models.User;
import org.example.models.enums.RequestStatus;
import org.example.repository.RequestRepository;
import org.example.repository.ReviewRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final RequestRepository requestRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, RequestRepository requestRepository) {
        this.reviewRepository = reviewRepository;
        this.requestRepository = requestRepository;
    }

    @Override
    public Long createReview(CreateReviewDto dto) throws SQLException {
        if (dto.getRating() == null || dto.getRating() < 1 || dto.getRating() > 5) {
            throw new IllegalArgumentException("Оценка должна быть от 1 до 5");
        }

        Request request = requestRepository.findById(dto.getRequestId())
                .orElseThrow(() -> new IllegalArgumentException("Заявка не найдена"));

        if (!Objects.equals(request.getStudent().getId(), dto.getStudentId())) {
            throw new IllegalArgumentException("Оставить отзыв может только автор заявки");
        }

        if (request.getStatus() != RequestStatus.ACCEPTED) {
            throw new IllegalArgumentException("Оставить отзыв можно только по принятой заявке");
        }

        if (reviewRepository.existsByRequestId(dto.getRequestId())) {
            throw new IllegalArgumentException("Вы уже оставили отзыв по этой заявке");
        }

        Long tutorId = request.getOffer().getAuthor().getId();

        Review review = Review.builder()
                .requestId(dto.getRequestId())
                .tutor(User.builder().id(tutorId).build())
                .student(User.builder().id(dto.getStudentId()).build())
                .rating(dto.getRating())
                .comment(dto.getComment())
                .build();

        reviewRepository.save(review);
        return tutorId;
    }

    @Override
    public List<Review> getReviewsByTutorId(Long tutorId) {
        return reviewRepository.findByTutorId(tutorId);
    }

    @Override
    public Double getAverageRatingByTutorId(Long tutorId) {
        return reviewRepository.getAverageRatingByTutorId(tutorId);
    }

    @Override
    public boolean existsByRequestId(Long requestId) {
        return reviewRepository.existsByRequestId(requestId);
    }
}