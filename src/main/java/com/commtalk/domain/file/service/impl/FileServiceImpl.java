package com.commtalk.domain.file.service.impl;

import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.common.util.CommonFileUtils;
import com.commtalk.domain.file.dto.request.FileCreateRequest;
import com.commtalk.domain.file.entity.File;
import com.commtalk.domain.file.entity.FileType;
import com.commtalk.domain.file.repository.FileRepository;
import com.commtalk.domain.file.repository.FileTypeRepository;
import com.commtalk.domain.file.service.FileService;
import com.commtalk.domain.post.entity.ActivityType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileTypeRepository fileTypeRepo;
    private final FileRepository fileRepo;

    @Value("${spring.servlet.multipart.location}")
    private String baseDirPath;

    @Override
    public void storeFile(FileType.TypeName typeName, Long refId, MultipartFile multipartFile) {
        // 파일 유형 조회
        FileType fileType = fileTypeRepo.findByTypeName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("파일 유형을 찾을 수 없습니다."));

        // 파일 저장
        FileCreateRequest createReq = CommonFileUtils.storeFile(baseDirPath, multipartFile);
        File file = File.create(fileType, refId, createReq);

        fileRepo.save(file);
    }

    @Override
    public void storeFiles(FileType.TypeName typeName, Long refId, List<MultipartFile> multipartFiles) {
        // 파일 유형 조회
        FileType fileType = fileTypeRepo.findByTypeName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("파일 유형을 찾을 수 없습니다."));

        // 파일 저장
        List<FileCreateRequest> createReqList = CommonFileUtils.storeFiles(baseDirPath, multipartFiles);
        List<File> files = createReqList.stream()
                .map(createReq -> File.create(fileType, refId, createReq))
                .toList();

        fileRepo.saveAll(files);
    }
}
