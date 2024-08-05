package com.commtalk.domain.file.repository;

import com.commtalk.domain.file.entity.File;
import com.commtalk.domain.file.entity.FileType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByTypeAndRefId(FileType type, Long refId);

    List<File> findAllByTypeAndRefId(FileType type, Long refId);

}
