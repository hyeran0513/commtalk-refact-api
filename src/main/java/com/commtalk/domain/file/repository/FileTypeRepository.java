package com.commtalk.domain.file.repository;

import com.commtalk.domain.file.entity.FileType;
import com.commtalk.domain.post.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileTypeRepository extends JpaRepository<FileType, Long> {

    Optional<FileType> findByName(FileType.TypeName name);

}
