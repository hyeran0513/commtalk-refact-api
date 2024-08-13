package com.commtalk.domain.file.entity;

import com.commtalk.common.entity.BaseEntity;
import com.commtalk.domain.file.dto.request.FileCreateRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "file")
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "file_type_id", nullable = false)
    private FileType type;

    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "file_path", nullable = false)
    private String filePath;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "save_file_name", nullable = false)
    private String saveFileName;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "file_size")
    private long fileSize;

    @Column(name = "deleted_yn")
    private boolean deletedYN;

    public static File create(FileType type, Long refId, FileCreateRequest createReq) {
        return File.builder()
                .type(type)
                .refId(refId)
                .filePath(createReq.getFilePath())
                .originalFileName(createReq.getOriginalFileName())
                .saveFileName(createReq.getSaveFileName())
                .fileExt(createReq.getFileExt())
                .fileSize(createReq.getFileSize())
                .build();
    }

}
