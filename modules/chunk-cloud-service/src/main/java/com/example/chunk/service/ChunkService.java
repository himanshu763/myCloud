package com.example.chunk.service;

import com.example.chunk.model.ChunkUploadStatus;
import com.example.chunk.model.File;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ChunkService {
    File uploadFile(MultipartFile file, String userId, List<String> providers) throws IOException;
    byte[] downloadFile(String fileId, String userId);
    boolean deleteFile(String fileId, String userId);
    List<File> getUserFiles(String userId);
}