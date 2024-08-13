package com.commtalk.domain.file.repository;

import com.commtalk.domain.file.entity.File;
import com.commtalk.domain.file.entity.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByRefIdAndTypeName(Long refId, FileType.TypeName typeName);

    List<File> findAllByRefIdAndTypeName(Long refId, FileType.TypeName typeName);

    void deleteAllByRefIdAndTypeName(Long refId, FileType.TypeName typeName);

}
