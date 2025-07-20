package org.example.plzdrawing.api.file.controller;

import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.api.file.dto.response.FileUploadResponse;
import org.example.plzdrawing.api.file.service.FileService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/files")
public class FileController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileUploadResponse> uploadFile(@RequestParam("file") MultipartFile file) {
        String fileUrl = fileService.uploadFile(file);
        FileUploadResponse response = new FileUploadResponse(
                fileUrl,
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType()
        );
        return ResponseEntity.ok(response);
    }
}
