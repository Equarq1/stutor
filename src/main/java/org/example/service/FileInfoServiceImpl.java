package org.example.service;

import org.example.dto.CreateFileInfoDto;
import org.example.models.FileInfo;
import org.example.models.User;
import org.example.models.enums.Role;
import org.example.repository.FileInfoRepository;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.UUID;

public class FileInfoServiceImpl implements FileInfoService {
    private final FileInfoRepository fileInfoRepository;
    private final String PATH_TO_STORAGE_DIRECTORY;

    public FileInfoServiceImpl(FileInfoRepository fileInfoRepository, String storagePath) {
        this.fileInfoRepository = fileInfoRepository;
        this.PATH_TO_STORAGE_DIRECTORY = storagePath;
        try {
            Files.createDirectories(Paths.get(PATH_TO_STORAGE_DIRECTORY));
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать директорию для хранения файлов", e);
        }
    }


    @Override
    public Long save(InputStream file, CreateFileInfoDto createFileInfoDto) throws SQLException {

        String originalName = createFileInfoDto.getOriginalFileName();
        String extension = "";
        if (originalName != null && originalName.lastIndexOf('.') > 0) {
            extension = originalName.substring(originalName.lastIndexOf('.'));
        }

        FileInfo fileInfo = FileInfo.builder()
                .originalFileName(createFileInfoDto.getOriginalFileName())
                .storageFileName(UUID.randomUUID().toString() + extension)
                .size(createFileInfoDto.getSize())
                .type(createFileInfoDto.getType())
                .build();

        try {
            Files.copy(file, Paths.get(PATH_TO_STORAGE_DIRECTORY + fileInfo.getStorageFileName()));
            try {
                fileInfoRepository.save(fileInfo);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return fileInfo.getId();
    }

    @Override
    public FileInfo getFileInfoById(long id) {
        return fileInfoRepository.findById(id);
    }

    @Override
    public void writeFileFromStorage(Long fileId, OutputStream outputStream) {
        FileInfo fileInfo = fileInfoRepository.findById(fileId);
        File file = new File(PATH_TO_STORAGE_DIRECTORY + fileInfo.getStorageFileName());
        try {
            Files.copy(file.toPath(), outputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean canUserAccess(Long fileId, User user) {
        if (user.getRole() == Role.ADMIN) {
            return true;
        }
        return fileInfoRepository.existsAccessibleForUser(fileId, user.getId());
    }
}
