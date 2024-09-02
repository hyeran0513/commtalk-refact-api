package com.commtalk.common.util;

import com.commtalk.domain.file.dto.request.FileCreateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class CommonFileUtils {

    public static FileCreateRequest storeFile(String baseDirPath, MultipartFile multipartFile) {
        // 디렉토리 준비
        File dir = new File(baseDirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // 파일 저장
        File file;
        FileCreateRequest createReq = null;
        if (!multipartFile.isEmpty()) {
            createReq = FileCreateRequest.of(multipartFile);

            int idx = 1; // 동일한 파일이 있을경우 붙을 변수
            createReq.setFilePath(baseDirPath, idx);
            while (Files.exists(Paths.get(createReq.getFilePath()))) { // 동일한 파일명이 없을 때까지 idx + 1
                createReq.setFilePath(baseDirPath, ++idx);
            }

            file = new File(createReq.getFilePath());
            try {
                multipartFile.transferTo(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return createReq;
    }

    public static List<FileCreateRequest> storeFiles(String baseDirPath, List<MultipartFile> multipartFiles) {
        List<FileCreateRequest> createReqList = new ArrayList<>();

        // 디렉토리 준비
        File dir = new File(baseDirPath);
        if (!dir.exists()) {
            dir.mkdir();
        }

        // 파일 저장
        File file;
        FileCreateRequest createReq;
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                createReq = FileCreateRequest.of(multipartFile);

                int idx = 1; // 동일한 파일이 있을경우 붙을 변수
                createReq.setFilePath(baseDirPath, idx);
                while (Files.exists(Paths.get(createReq.getFilePath()))) { // 동일한 파일명이 없을 때까지 idx + 1
                    createReq.setFilePath(baseDirPath, ++idx);
                }

                file = new File(createReq.getFilePath());
                try {
                    multipartFile.transferTo(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                createReqList.add(createReq);
            }
        }

        return createReqList;
    }

    public static void deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
    }

}
