package com.commtalk.domain.file.service.impl;

import com.commtalk.domain.file.repository.FileRepository;
import com.commtalk.domain.file.repository.FileTypeRepository;
import com.commtalk.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileTypeRepository fileTypeRepo;
    private final FileRepository fileRepo;

}
