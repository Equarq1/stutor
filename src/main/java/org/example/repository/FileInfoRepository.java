package org.example.repository;

import org.example.models.FileInfo;

public interface FileInfoRepository extends CrudRepository<FileInfo> {
    FileInfo findById(Long id);
}
