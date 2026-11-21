package org.example.repository;

import org.example.models.FileInfo;
import org.example.models.Review;
import org.example.models.User;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepositoryJdbcImpl implements ReviewRepository {

    private final DataSource dataSource;

    private static final String SQL_INSERT = """
            INSERT INTO reviews (request_id, tutor_id, student_id, rating, comment, created_at)
            VALUES (?, ?, ?, ?, ?, ?)
            """;

    private static final String SQL_SELECT_BY_TUTOR_ID = """
            SELECT rev.id, rev.request_id, rev.rating, rev.comment, rev.created_at,
                   s.id AS student_id, s.full_name AS student_name,
                   f.id AS avatar_id, f.storage_file_name AS avatar_file
            FROM reviews rev
            JOIN users s ON rev.student_id = s.id
            LEFT JOIN file_info f ON s.avatar_id = f.id
            WHERE rev.tutor_id = ?
            ORDER BY rev.created_at DESC
            """;

    private static final String SQL_EXISTS_BY_REQUEST_ID =
            "SELECT 1 FROM reviews WHERE request_id = ?";

    private static final String SQL_AVG_RATING_BY_TUTOR_ID =
            "SELECT AVG(rating) FROM reviews WHERE tutor_id = ?";

    public ReviewRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void save(Review review) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_INSERT)) {
            if (review.getRequestId() != null) {
                ps.setLong(1, review.getRequestId());
            } else {
                ps.setNull(1, Types.BIGINT);
            }
            ps.setLong(2, review.getTutor().getId());
            ps.setLong(3, review.getStudent().getId());
            ps.setShort(4, review.getRating());
            ps.setString(5, review.getComment());
            ps.setObject(6, LocalDateTime.now());
            ps.executeUpdate();
        }
    }

    @Override
    public List<Review> findByTutorId(Long tutorId) {
        List<Review> reviews = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_SELECT_BY_TUTOR_ID)) {
            ps.setLong(1, tutorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    reviews.add(mapRowToReview(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении отзывов", e);
        }
        return reviews;
    }

    @Override
    public boolean existsByRequestId(Long requestId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_EXISTS_BY_REQUEST_ID)) {
            ps.setLong(1, requestId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования отзыва", e);
        }
    }

    @Override
    public Double getAverageRatingByTutorId(Long tutorId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_AVG_RATING_BY_TUTOR_ID)) {
            ps.setLong(1, tutorId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next() && rs.getObject(1) != null) {
                    return rs.getDouble(1);
                }
                return null;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при подсчете среднего рейтинга", e);
        }
    }

    private Review mapRowToReview(ResultSet rs) throws SQLException {
        FileInfo avatar = null;
        if (rs.getObject("avatar_id") != null) {
            avatar = FileInfo.builder()
                    .id(rs.getLong("avatar_id"))
                    .storageFileName(rs.getString("avatar_file"))
                    .build();
        }

        User student = User.builder()
                .id(rs.getLong("student_id"))
                .fullName(rs.getString("student_name"))
                .avatar(avatar)
                .build();

        Long requestId = rs.getObject("request_id") != null ? rs.getLong("request_id") : null;

        return Review.builder()
                .id(rs.getLong("id"))
                .requestId(requestId)
                .student(student)
                .rating(rs.getShort("rating"))
                .comment(rs.getString("comment"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .build();
    }
}