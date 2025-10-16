package org.example.plzdrawing.util.s3;

import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Hidden
@RestController
@RequiredArgsConstructor
@RequestMapping("/file")
public class S3TestController {
    private final S3Service s3Service;
    @PostMapping
    public ResponseEntity<String> uploadFile(MultipartFile multipartFile){
        return ResponseEntity.ok((s3Service.uploadFile(1L, multipartFile)));
    }
}
