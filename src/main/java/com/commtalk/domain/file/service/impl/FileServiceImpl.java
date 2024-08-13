package com.commtalk.domain.file.service.impl;

import com.commtalk.common.exception.CustomException;
import com.commtalk.common.exception.EntityNotFoundException;
import com.commtalk.common.exception.ErrorCode;
import com.commtalk.common.util.CommonFileUtils;
import com.commtalk.domain.file.dto.request.FileCreateRequest;
import com.commtalk.domain.file.entity.File;
import com.commtalk.domain.file.entity.FileType;
import com.commtalk.domain.file.repository.FileRepository;
import com.commtalk.domain.file.repository.FileTypeRepository;
import com.commtalk.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileTypeRepository fileTypeRepo;
    private final FileRepository fileRepo;

    @Value("${spring.servlet.multipart.location}")
    private String baseDirPath;

    @Value("${base.url}")
    private String baseUrl;

    @Override
    public void storeFile(FileType.TypeName typeName, Long refId, MultipartFile multipartFile) {
        // 이전 파일 삭제
        deleteFiles(typeName, refId);

        // 파일 유형 조회
        FileType fileType = fileTypeRepo.findByName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("파일 유형을 찾을 수 없습니다."));

        // 파일 저장
        FileCreateRequest createReq = CommonFileUtils.storeFile(baseDirPath, multipartFile);
        File file = File.create(fileType, refId, createReq);

        fileRepo.save(file);
    }

    @Override
    public void storeFiles(FileType.TypeName typeName, Long refId, List<MultipartFile> multipartFiles) {
        // 이전 파일(리스트) 삭제
        deleteFiles(typeName, refId);

        // 파일 유형 조회
        FileType fileType = fileTypeRepo.findByName(typeName)
                .orElseThrow(() -> new EntityNotFoundException("파일 유형을 찾을 수 없습니다."));

        // 파일 저장
        List<FileCreateRequest> createReqList = CommonFileUtils.storeFiles(baseDirPath, multipartFiles);
        List<File> files = createReqList.stream()
                .map(createReq -> File.create(fileType, refId, createReq))
                .toList();

        fileRepo.saveAll(files);
    }

    @Override
    public void deleteFiles(FileType.TypeName name, Long refId) {
         // 파일(리스트) 삭제
        fileRepo.deleteAllByRefIdAndTypeName(refId, name);
    }

    @Override
    public String getFileUrl(FileType.TypeName name, Long refId) {
        // 파일 조회
        File file = fileRepo.findByRefIdAndTypeName(refId, name)
                .orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다."));

        return baseUrl + "/files/" + file.getId();
    }

    @Override
    public List<String> getFileUrls(FileType.TypeName name, Long refId) {
        // 파일 리스트 조회
        List<File> files = fileRepo.findAllByRefIdAndTypeName(refId, name);

        List<String> fileUrls = new ArrayList<>();
        for (File file : files) {
            fileUrls.add(baseUrl + "/files/" + file.getId());
        }
        return fileUrls;
    }

    @Override
    public Resource getFile(Long fileId) {
        // 파일 조회
        File file = fileRepo.findById(fileId)
                .orElseThrow(() -> new EntityNotFoundException("파일을 찾을 수 없습니다."));

        Resource resource;
        try {
            Path path = Paths.get(file.getFilePath());
            resource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new CustomException(ErrorCode.INVALID_URL);
        }
        return resource;
    }

}
