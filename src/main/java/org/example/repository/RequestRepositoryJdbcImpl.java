package org.example.repository;

import org.example.models.*;
import org.example.models.enums.RequestStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


public class RequestRepositoryJdbcImpl implements RequestRepository {

    private final DataSource dataSource;
    private static final String SQL_INSERT_INTO_REQUESTS = """
    INSERT INTO requests (offer_id, subject_id, message,
     created_at, status, student_id, attachment_id) values (?, ?, ?, ?, ?, ?, ?)
    """;

    private static final String SQL_BASE_SELECT = """
    SELECT r.id, r.message, r.created_at, r.status,
           o.id AS offer_id, o.title AS offer_title,
           author.id AS author_id, author.full_name AS author_name, author.contact AS author_contact,
           s.id AS subject_id, s.name AS subject_name,
           u.id AS student_id, u.full_name AS student_name, u.contact AS student_contact,
           f.id AS file_id, f.original_file_name, f.storage_file_name
    FROM requests r
    JOIN offers o ON r.offer_id = o.id
    JOIN users author ON o.author_id = author.id
    JOIN subjects s ON r.subject_id = s.id
    JOIN users u ON r.student_id = u.id
    LEFT JOIN file_info f ON r.attachment_id = f.id
    """;

    private static final String SQL_FIND_BY_STUDENT = SQL_BASE_SELECT + " WHERE r.student_id = ? ORDER BY r.created_at DESC";
    private static final String SQL_FIND_BY_OFFER_AUTHOR = SQL_BASE_SELECT + " WHERE o.author_id = ? AND r.status != 'REJECTED' ORDER BY r.created_at DESC";
    private static final String SQL_FIND_BY_ID = SQL_BASE_SELECT + " WHERE r.id = ?";
    private static final String SQL_UPDATE_STATUS = "UPDATE requests SET status = ? WHERE id = ?";

    public RequestRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }


    @Override
    public void save(Request request) throws SQLException {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT_INTO_REQUESTS)
        ) {
            preparedStatement.setLong(1, request.getOffer().getId());
            preparedStatement.setLong(2, request.getSubject().getId());
            preparedStatement.setString(3, request.getMessage());
            preparedStatement.setObject(4, LocalDateTime.now());
            preparedStatement.setString(5, RequestStatus.PENDING.name());
            preparedStatement.setLong(6, request.getStudent().getId());

            if (request.getAttachment() != null && request.getAttachment().getId() != null) {
                preparedStatement.setLong(7, request.getAttachment().getId());
            }
            else {
                preparedStatement.setNull(7, Types.BIGINT);
            }
            preparedStatement.executeUpdate();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Request> findByStudentId(Long studentId) {
        return executeSelectList(SQL_FIND_BY_STUDENT, studentId);
    }

    @Override
    public List<Request> findByOfferAuthorId(Long offerAuthorId) {
        return executeSelectList(SQL_FIND_BY_OFFER_AUTHOR, offerAuthorId);
    }

    @Override
    public Optional<Request> findById(Long id) {
        List<Request> list = executeSelectList(SQL_FIND_BY_ID, id);
        return list.isEmpty() ? Optional.empty() : Optional.of(list.getFirst());
    }

    @Override
    public void updateStatus(Long requestId, RequestStatus status) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_UPDATE_STATUS)) {
            ps.setString(1, status.name());
            ps.setLong(2, requestId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка обновления статуса", e);
        }
    }

    private List<Request> executeSelectList(String sql, Long paramId) {
        List<Request> list = new ArrayList<>();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setLong(1, paramId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(rowMapperToRequest(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении заявок", e);
        }
        return list;
    }

    private Request rowMapperToRequest(ResultSet rs) throws SQLException {

        User author = User.builder()
                .id(rs.getLong("author_id"))
                .fullName(rs.getString("author_name"))
                .contact(rs.getString("author_contact"))
                .build();

        Offer offer = Offer.builder()
                .id(rs.getLong("offer_id"))
                .title(rs.getString("offer_title"))
                .author(author)
                .build();

        Subject subject = Subject.builder()
                .id(rs.getInt("subject_id"))
                .name(rs.getString("subject_name"))
                .build();

        User student = User.builder()
                .id(rs.getLong("student_id"))
                .fullName(rs.getString("student_name"))
                .contact(rs.getString("student_contact"))
                .build();

        FileInfo attachment = null;
        if (rs.getObject("file_id") != null) {
            attachment = FileInfo.builder()
                    .id(rs.getLong("file_id"))
                    .originalFileName(rs.getString("original_file_name"))
                    .storageFileName(rs.getString("storage_file_name"))
                    .build();
        }

        return Request.builder()
                .id(rs.getLong("id"))
                .message(rs.getString("message"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .status(RequestStatus.valueOf(rs.getString("status")))
                .offer(offer)
                .subject(subject)
                .student(student)
                .attachment(attachment)
                .build();
    }

    private static final String SQL_EXISTS =
            "SELECT 1 FROM requests WHERE offer_id = ? AND subject_id = ? AND student_id = ?";

    @Override
    public boolean existsByOfferAndSubjectAndStudent(Long offerId, Integer subjectId, Long studentId) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement ps = connection.prepareStatement(SQL_EXISTS)) {
            ps.setLong(1, offerId);
            ps.setInt(2, subjectId);
            ps.setLong(3, studentId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка проверки существования заявки", e);
        }
    }
}
