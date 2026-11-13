package org.example.service;

import org.example.dto.CreateFileInfoDto;
import org.example.models.FileInfo;

import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

public interface FileInfoService {
    Long save(InputStream file, CreateFileInfoDto createFileInfoDto) throws SQLException;
    FileInfo getFileInfoById(long id);
    void writeFileFromStorage(Long fileId, OutputStream outputStream);

}
