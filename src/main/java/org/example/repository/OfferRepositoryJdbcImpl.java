package org.example.repository;

import org.example.models.*;
import org.example.models.enums.OfferFormat;
import org.example.models.enums.OfferStatus;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class OfferRepositoryJdbcImpl implements OfferRepository {
    private final DataSource dataSource;

    private static final String SQL_BASE = """
        SELECT o.id, o.title, o.description, o.price, o.created_at, o.format, o.status,
               u.id AS author_id, u.full_name AS author_name,
               a.id AS avatar_id, a.storage_file_name AS avatar_file,
               c.id AS category_id, c.name AS category_name,
               f.id AS file_id, f.original_file_name, f.storage_file_name
        FROM offers o
        JOIN users u ON o.author_id = u.id
        JOIN offer_categories c ON o.category_id = c.id
        LEFT JOIN file_info a ON u.avatar_id = a.id
        LEFT JOIN file_info f ON o.attachment_id = f.id
        """;

    private static final String SQL_SELECT_ALL = SQL_BASE + " WHERE o.status = 'ACTIVE'";
    private static final String SQL_SELECT_WITH_LIMIT = SQL_BASE + " WHERE o.status = 'ACTIVE' ORDER BY o.created_at DESC LIMIT ?";
    private static final String SQL_SELECT_BY_ID = SQL_BASE + " WHERE o.id = ?";

    private static final String SQL_SELECT_SUBJECTS = """
    SELECT s.id, s.name, s.course,
           f.id AS faculty_id, f.name AS faculty_name
    FROM subjects s
    JOIN offer_subjects os ON s.id = os.subject_id
    JOIN faculties f ON s.faculty_id = f.id
    WHERE os.offer_id = ?
    """;

    private static final String SQL_INSERT_OFFER = """
        INSERT INTO offers (title, description, price, created_at, format, status, author_id, category_id, attachment_id) 
        VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
        """;

    private static final String SQL_INSERT_OFFER_SUBJECT = """
        INSERT INTO offer_subjects (offer_id, subject_id) VALUES (?, ?)
        """;

    public OfferRepositoryJdbcImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Offer> findAll() {
        try (Connection connection = dataSource.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(SQL_SELECT_ALL)) {

            List<Offer> list = new ArrayList<>();
            while (resultSet.next()) {
                Offer offer = mapRowToOffer(resultSet);
                offer.setSubjects(getSubjectsForOffer(offer.getId(), connection));
                list.add(offer);
            }
            return list;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка получения списка всех объявлений", e);
        }
    }

    @Override
    public List<Offer> findAllLastWithLimit(int n) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_WITH_LIMIT)) {

            preparedStatement.setInt(1, n);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                List<Offer> list = new ArrayList<>();
                while (resultSet.next()) {
                    Offer offer = mapRowToOffer(resultSet);
                    offer.setSubjects(getSubjectsForOffer(offer.getId(), connection));
                    list.add(offer);
                }
                return list;
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении последних объявлений", e);
        }
    }

    @Override
    public Offer findOfferById(long id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {

            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    Offer offer = mapRowToOffer(resultSet);
                    offer.setSubjects(getSubjectsForOffer(offer.getId(), connection));
                    return offer;
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске объявления по ID", e);
        }
    }


    @Override
    public void save(Offer offer) {
        try (Connection connection = dataSource.getConnection()) {

            connection.setAutoCommit(false);

            try (PreparedStatement offerPreparedStatement = connection.prepareStatement(SQL_INSERT_OFFER, Statement.RETURN_GENERATED_KEYS)) {

                offerPreparedStatement.setString(1, offer.getTitle());
                offerPreparedStatement.setString(2, offer.getDescription());
                offerPreparedStatement.setBigDecimal(3, offer.getPrice());
                offerPreparedStatement.setObject(4, offer.getCreatedAt());
                offerPreparedStatement.setString(5, offer.getFormat().name());
                offerPreparedStatement.setString(6, offer.getStatus().name());
                offerPreparedStatement.setLong(7, offer.getAuthor().getId());
                offerPreparedStatement.setInt(8, offer.getCategory().getId());

                if (offer.getAttachment() != null) {
                    offerPreparedStatement.setLong(9, offer.getAttachment().getId());
                } else {
                    offerPreparedStatement.setNull(9, Types.BIGINT);
                }

                offerPreparedStatement.executeUpdate();

                try (ResultSet generatedKeys = offerPreparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        offer.setId(generatedKeys.getLong(1));
                    } else {
                        throw new SQLException("Создание объявления не удалось, ID не получен.");
                    }
                }

                if (offer.getSubjects() != null) {
                    try (PreparedStatement subjectStmt = connection.prepareStatement(SQL_INSERT_OFFER_SUBJECT)) {
                        for (Subject subject : offer.getSubjects()) {
                            subjectStmt.setLong(1, offer.getId());
                            subjectStmt.setInt(2, subject.getId());
                            subjectStmt.addBatch();
                        }
                        subjectStmt.executeBatch();
                    }
                }

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                throw new RuntimeException("Ошибка при сохранении объявления. Транзакция отменена.", e);
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            throw new RuntimeException("Ошибка базы данных", e);
        }
    }

    private Offer mapRowToOffer(ResultSet rs) throws SQLException {
        Category category = Category.builder()
                .id(rs.getInt("category_id"))
                .name(rs.getString("category_name"))
                .build();

        FileInfo avatar = null;
        if (rs.getObject("avatar_id") != null) {
            avatar = FileInfo.builder()
                    .id(rs.getLong("avatar_id"))
                    .storageFileName(rs.getString("avatar_file"))
                    .build();
        }

        User author = User.builder()
                .id(rs.getLong("author_id"))
                .fullName(rs.getString("author_name"))
                .avatar(avatar)
                .build();

        FileInfo attachment = null;
        if (rs.getObject("file_id") != null) {
            attachment = FileInfo.builder()
                    .id(rs.getLong("file_id"))
                    .originalFileName(rs.getString("original_file_name"))
                    .storageFileName(rs.getString("storage_file_name"))
                    .build();
        }

        return Offer.builder()
                .id(rs.getLong("id"))
                .title(rs.getString("title"))
                .description(rs.getString("description"))
                .price(rs.getBigDecimal("price"))
                .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                .format(OfferFormat.valueOf(rs.getString("format").toUpperCase()))
                .status(OfferStatus.valueOf(rs.getString("status").toUpperCase()))
                .category(category)
                .author(author)
                .attachment(attachment)
                .build();
    }

    private List<Subject> getSubjectsForOffer(Long offerId, Connection connection) throws SQLException {
        List<Subject> subjects = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_SUBJECTS)) {
            preparedStatement.setLong(1, offerId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                Faculty faculty = Faculty.builder()
                        .id(resultSet.getInt("faculty_id"))
                        .name(resultSet.getString("faculty_name"))
                        .build();
                subjects.add(
                        Subject.builder()
                                .id(resultSet.getInt("id"))
                                .name(resultSet.getString("name"))
                                .course(resultSet.getInt("course"))
                                .faculty(faculty)
                                .build()
                );
            }
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return subjects;
    }
}