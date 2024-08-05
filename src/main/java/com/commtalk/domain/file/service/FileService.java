package com.commtalk.domain.file.service;

import com.commtalk.domain.file.dto.request.FileSimpleDTO;
import com.commtalk.domain.file.entity.FileType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    void storeFile(FileType.TypeName typeName, Long refId, MultipartFile files);

    void storeFiles(FileType.TypeName typeName, Long refId, List<MultipartFile> files);

    FileSimpleDTO getFilePath(FileType.TypeName typeName, Long refId);

    List<FileSimpleDTO> getFilePaths(FileType.TypeName typeName, Long refId);

}
