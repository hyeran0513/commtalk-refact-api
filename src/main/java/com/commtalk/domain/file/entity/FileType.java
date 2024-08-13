package com.commtalk.domain.file.entity;

import com.commtalk.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "file_type")
public class FileType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "file_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_name", nullable = false)
    private TypeName name;

    public enum TypeName {
        PROFILE, POST
    }

}
