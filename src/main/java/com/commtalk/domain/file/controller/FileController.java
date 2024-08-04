package com.commtalk.domain.file.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "file", description = "파일 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/files")
public class FileController {
}
