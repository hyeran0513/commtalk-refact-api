package com.commtalk.domain.post.entity;

import com.commtalk.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
@Entity
@Table(name = "activity_type")
public class ActivityType extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "activity_type_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type_name", nullable = false)
    private TypeName typeName;

    public enum TypeName {
        POST_LIKE, COMMENT_LIKE, POST_SCRAP
    }

}
