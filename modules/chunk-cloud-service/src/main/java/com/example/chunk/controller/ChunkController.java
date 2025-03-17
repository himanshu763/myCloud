package com.example.chunk.controller;

import com.example.chunk.model.File;
import com.example.chunk.service.ChunkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/chunks")
public class ChunkController {
    
    private final ChunkService chunkService;
    
    @Autowired
    public ChunkController(ChunkService chunkService) {
        this.chunkService = chunkService;
    }
    
    @PostMapping("/upload")
    public ResponseEntity<File> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") String userId,
            @RequestParam("providers") List<String> providers) throws IOException {
        
        File uploadedFile = chunkService.uploadFile(file, userId, providers);
        return ResponseEntity.ok(uploadedFile);
    }
    
    @GetMapping("/download/{fileId}")
    public ResponseEntity<byte[]> downloadFile(
            @PathVariable String fileId,
            @RequestParam("userId") String userId) {
        
        byte[] fileContent = chunkService.downloadFile(fileId, userId);
        
        // TODO: Get file metadata for proper headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDispositionFormData("attachment", "file_" + fileId);
        
        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }
    
    @DeleteMapping("/{fileId}")
    public ResponseEntity<Void> deleteFile(
            @PathVariable String fileId,
            @RequestParam("userId") String userId) {
        
        boolean deleted = chunkService.deleteFile(fileId, userId);
        
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<File>> getUserFiles(@PathVariable String userId) {
        List<File> files = chunkService.getUserFiles(userId);
        return ResponseEntity.ok(files);
    }
}