package com.commtalk.domain.file.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "파일 URL 정보")
public class FileUrlDTO {

    @Schema(description = "파일 URL")
    private String fileUrl;

    public static FileUrlDTO of(String fileUrl) {
        return FileUrlDTO.builder()
                .fileUrl(fileUrl)
                .build();
    }

}
