package com.commtalk.domain.file.controller;

import com.commtalk.common.dto.ResponseDTO;
import com.commtalk.domain.file.dto.request.FileSimpleDTO;
import com.commtalk.domain.file.entity.File;
import com.commtalk.domain.file.entity.FileType;
import com.commtalk.domain.file.service.FileService;
import com.commtalk.domain.post.service.PostService;
import com.commtalk.security.JwtAuthenticationProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "file", description = "파일 API")
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/files")
public class FileController {

    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    
    private final FileService fileSvc;
    private final PostService postSvc;

    @Operation(summary = "회원 프로필 사진 업로드")
    @PostMapping(path = "/profile")
    public ResponseEntity<ResponseDTO<String>> uploadProfile(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        fileSvc.storeFile(FileType.TypeName.PROFILE, memberId, file);
        return ResponseDTO.of(HttpStatus.OK, "프로필 사진을 업로드 했습니다.");
    }

    @Operation(summary = "회원 프로필 사진 조회")
    @GetMapping(path = "/profile")
    public ResponseEntity<FileSimpleDTO> getProfile(HttpServletRequest request) {
        Long memberId = jwtAuthenticationProvider.getMemberId(request);
        FileSimpleDTO fileDto = fileSvc.getFilePath(FileType.TypeName.PROFILE, memberId);
        return ResponseEntity.ok(fileDto);
    }

    @Operation(summary = "게시글 파일(리스트) 업로드")
    @PostMapping(path = "/post/{postId}")
    public ResponseEntity<ResponseDTO<String>> uploadPostFiles(@RequestParam("files") List<MultipartFile> files, @PathVariable Long postId) {
        postSvc.isExistsPost(postId); // 게시글이 존재하는지 확인
        fileSvc.storeFiles(FileType.TypeName.POST, postId, files);
        return ResponseDTO.of(HttpStatus.OK, "게시글 첨부 파일(리스트)을 업로드 했습니다.");
    }

    @Operation(summary = "게시글 파일(리스트) 조회")
    @GetMapping(path = "/post/{postId}")
    public ResponseEntity<List<FileSimpleDTO>> getPostFiles(@PathVariable Long postId) {
        List<FileSimpleDTO> fileDtoList = fileSvc.getFilePaths(FileType.TypeName.POST, postId);
        return ResponseEntity.ok(fileDtoList);
    }

}
