package com.ulutman.controller;

import com.ulutman.model.enums.MediaFileType;
import com.ulutman.service.MinioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/S3")
@Tag(name = "Upload")
@SecurityRequirement(name = "Authorization")
@RequiredArgsConstructor
public class S3Controller {

    private final MinioService minioService;

    @Operation(summary = "Загрузить файлы", description = "Загружает изображения и возвращает presigned URL")
    @ApiResponse(responseCode = "200", description = "Список presigned URL загруженных файлов")
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String>> uploadFiles(
            @RequestPart("files") MultipartFile[] files,
            @RequestParam(defaultValue = "PUBLISH_IMAGE") MediaFileType type) {

        List<String> urls = Arrays.stream(files)
                .map(file -> {
                    String objectKey = minioService.upload(file, type, "general");
                    return minioService.presign(objectKey);
                })
                .toList();

        return ResponseEntity.ok(urls);
    }
}
