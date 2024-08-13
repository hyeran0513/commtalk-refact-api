package com.commtalk.domain.file.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Builder
@Schema(description = "파일 생성 정보")
public class FileCreateRequest {

    @Schema(description = "파일을 첨부하는 도메인 식별자")
    private Long refId;

    @Schema(description = "파일 경로")
    private String filePath;

    @Schema(description = "파일 원본 이름")
    private String originalFileName;

    @Schema(description = "파일 저장 이름")
    private String saveFileName;

    @Schema(description = "파일 확장자")
    private String fileExt;

    @Schema(description = "파일 크기")
    private long fileSize;

    public static FileCreateRequest of(MultipartFile file) {
        String fileName = file.getOriginalFilename();

        return FileCreateRequest.builder()
                .originalFileName(fileName)
                .fileExt(fileName.substring(fileName.lastIndexOf(".") + 1))
                .fileSize(file.getSize())
                .build();
    }

    public void setFilePath(String fileDirPath, int idx) {
        if (idx >= 2) {
            this.saveFileName = originalFileName.substring(0, originalFileName.lastIndexOf(".")) + "(" + idx + ")." + fileExt;
        } else {
            this.saveFileName = originalFileName;
        }
        this.filePath = fileDirPath + saveFileName;
    }

}
