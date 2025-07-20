package org.example.plzdrawing.util.s3.service;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.example.plzdrawing.common.exception.RestApiException;
import org.example.plzdrawing.util.s3.dto.S3Component;
import org.example.plzdrawing.api.file.service.FileService;
import org.example.plzdrawing.util.s3.exception.S3ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

@Service
@RequiredArgsConstructor
public class S3Service implements FileService {
    private final S3Client s3Client;
    private final S3Component s3Component;
    private final S3Presigner s3Presigner;

    @Value("${spring.cloud.aws.s3.url-duration}")
    private long presignedUrlDurationMinutes;

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = generateUniqueFileName(file.getOriginalFilename());
        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Component.getBucket())
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .key(fileName)
                    .build();
            RequestBody requestBody = RequestBody.fromBytes(file.getBytes());

            s3Client.putObject(putObjectRequest, requestBody);
        } catch (IOException e) {
            throw new RestApiException(S3ErrorCode.UNPROCESSABLE_IMAGE.getErrorCode());
        } catch (S3Exception e) {
            throw new RestApiException(S3ErrorCode.UPLOAD_FAILED.getErrorCode());
        }

        return getFileUrl(fileName);
    }

    @Override
    public void deleteFile(String fileName) {
        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                    .bucket(s3Component.getBucket())
                    .key(fileName)
                    .build();

            s3Client.deleteObject(deleteObjectRequest);
        } catch (S3Exception e) {
            throw new RestApiException(S3ErrorCode.DELETE_FAILED.getErrorCode());
        }
    }

    private String getFileUrl(String fileName) {
        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Component.getBucket())
                    .key(fileName)
                    .build();

            GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(presignedUrlDurationMinutes))
                    .getObjectRequest(getObjectRequest)
                    .build();

            URL presignedUrl = s3Presigner.presignGetObject(presignRequest).url();
            return presignedUrl.toString();
        } catch (S3Exception e) {
            throw new RestApiException(S3ErrorCode.GET_URL_FAILED.getErrorCode());
        }
    }

    private String generateUniqueFileName(String originalFileName) {
        return UUID.randomUUID() + "-" + originalFileName;
    }
}

