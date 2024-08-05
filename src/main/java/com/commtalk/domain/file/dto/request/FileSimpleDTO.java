package com.commtalk.domain.file.dto.request;

import com.commtalk.domain.file.entity.File;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
@Schema(description = "파일 기본 정보")
public class FileSimpleDTO {

    @Schema(description = "파일 식별자")
    private Long fileId;

    @Setter
    @Schema(description = "파일 URL")
    private String fileUrl;

    public static FileSimpleDTO of(File file) {
        return FileSimpleDTO.builder()
                .fileId(file.getId())
                .build();
    }

}
