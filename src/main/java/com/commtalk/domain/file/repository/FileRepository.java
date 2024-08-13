package com.commtalk.domain.file.repository;

import com.commtalk.domain.file.entity.File;
import com.commtalk.domain.file.entity.FileType;
import com.commtalk.domain.post.entity.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FileRepository extends JpaRepository<File, Long> {

    Optional<File> findByRefIdAndTypeName(Long refId, FileType.TypeName name);

    List<File> findAllByRefIdAndTypeName(Long refId, FileType.TypeName name);

    void deleteAllByRefIdAndTypeName(Long refId, FileType.TypeName name);

}
