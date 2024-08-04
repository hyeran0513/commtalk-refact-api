package com.commtalk.domain.file.entity;

import com.commtalk.common.entity.BaseEntity;
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "file_type_id", nullable = false)
    private FileType type;

    @Column(name = "ref_id")
    private Long refId;

    @Column(name = "file_path")
    private String filePath;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "save_file_name")
    private String saveFileName;

    @Column(name = "file_ext")
    private String fileExt;

    @Column(name = "deleted_yn")
    private boolean deletedYN;


}
