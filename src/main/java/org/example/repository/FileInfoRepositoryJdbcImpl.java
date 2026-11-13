    package org.example.repository;

    import org.example.models.FileInfo;
    import org.springframework.jdbc.core.RowMapper;
    import org.springframework.security.core.parameters.P;

    import javax.sql.DataSource;
    import java.sql.Connection;
    import java.sql.PreparedStatement;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
    import java.util.List;

    public class FileInfoRepositoryJdbcImpl implements FileInfoRepository {
        private final DataSource dataSource;
        private static final String SQL_INSERT = "INSERT INTO file_info (storage_file_name, original_file_name, type, size, created_at)" +
                "VALUES (?, ?, ?, ?, NOW())";
        private static final String SQL_SELECT_BY_ID = "SELECT * from file_info WHERE id = ?";

        public FileInfoRepositoryJdbcImpl(DataSource dataSource) {
            this.dataSource = dataSource;
        }


        @Override
        public FileInfo findById(Long id) {
            try (Connection connection = dataSource.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(SQL_SELECT_BY_ID)) {
                    preparedStatement.setLong(1, id);
                    ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) return rowMapperToFileInfo(resultSet);
                return null;
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void save(FileInfo fileInfo) throws SQLException {
            try (Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement = connection.prepareStatement(SQL_INSERT)) {
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
                        .build();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }


    }
