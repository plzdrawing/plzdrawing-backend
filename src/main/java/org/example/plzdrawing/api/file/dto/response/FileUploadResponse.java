package org.example.plzdrawing.api.file.dto.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class FileUploadResponse {
    private String fileUrl;
    private String fileName;
    private long fileSize;
    private String mimeType;
}