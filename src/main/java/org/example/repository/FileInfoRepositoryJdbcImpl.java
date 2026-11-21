    package org.example.repository;

    import org.example.models.FileInfo;
    import javax.sql.DataSource;
    import java.sql.*;

    public class FileInfoRepositoryJdbcImpl implements FileInfoRepository {
        private final DataSource dataSource;
        private static final String SQL_INSERT = "INSERT INTO file_info (storage_file_name, original_file_name, type, size, created_at)" +
                "VALUES (?, ?, ?, ?, NOW())";
        private static final String SQL_SELECT_BY_ID = "SELECT * from file_info WHERE id = ?";

        private static final String SQL_EXISTS_ACCESSIBLE = """
    SELECT EXISTS (
        SELECT 1 FROM users WHERE avatar_id = ?
        UNION ALL
        SELECT 1 FROM offers WHERE attachment_id = ?
        UNION ALL
        SELECT 1 FROM requests r
        JOIN offers o ON o.id = r.offer_id
        WHERE r.attachment_id = ? AND (r.student_id = ? OR o.author_id = ?)
    )
    """;

        public FileInfoRepositoryJdbcImpl(DataSource dataSource) {
            this.dataSource = dataSource;
        }


        @Override
        public FileInfo findById(Long id) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
                preparedStatement.setLong(1, id);
                try (ResultSet resultSet = preparedStatement.executeQuery();) {
                    if (resultSet.next()) return rowMapperToFileInfo(resultSet);
                    return null;
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void save(FileInfo fileInfo) throws SQLException {
            try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT, Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, fileInfo.getStorageFileName());
                preparedStatement.setString(2, fileInfo.getOriginalFileName());
                preparedStatement.setString(3, fileInfo.getType());
                preparedStatement.setLong(4, fileInfo.getSize());
                preparedStatement.executeUpdate();

                try (ResultSet generatedKeys = preparedStatement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        fileInfo.setId(generatedKeys.getLong(1));
                    }
                }
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        private FileInfo rowMapperToFileInfo(ResultSet resultSet) {
            try {
                return FileInfo.builder()
                        .id(resultSet.getLong("id"))
                        .originalFileName(resultSet.getString("original_file_name"))
                        .storageFileName(resultSet.getString("storage_file_name"))
                        .size(resultSet.getLong("size"))
                        .type(resultSet.getString("type"))
                        .createdAt(resultSet.getTimestamp("created_at").toLocalDateTime())
                        .build();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public boolean existsAccessibleForUser(Long fileId, Long userId) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement ps = connection.prepareStatement(SQL_EXISTS_ACCESSIBLE)) {
                ps.setLong(1, fileId);
                ps.setLong(2, fileId);
                ps.setLong(3, fileId);
                ps.setLong(4, userId);
                ps.setLong(5, userId);
                try (ResultSet rs = ps.executeQuery()) {
                    return rs.next() && rs.getBoolean(1);
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }
