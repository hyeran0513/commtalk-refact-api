package com.commtalk.domain.file.service;

import com.commtalk.domain.file.entity.FileType;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    void storeFile(FileType.TypeName typeName, Long refId, MultipartFile files);

    void storeFiles(FileType.TypeName typeName, Long refId, List<MultipartFile> files);

    void deleteFiles(FileType.TypeName typeName, Long refId);

    String getFileUrl(FileType.TypeName typeName, Long refId);

    List<String> getFileUrls(FileType.TypeName typeName, Long refId);

    Resource getFile(Long fileId);

}
