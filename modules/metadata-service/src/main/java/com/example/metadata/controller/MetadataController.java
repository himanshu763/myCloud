package com.example.metadata.controller;

import com.example.metadata.model.ChunkMetadata;
import com.example.metadata.model.FileMetadata;
import com.example.metadata.service.MetadataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metadata")
public class MetadataController {

    private final MetadataService metadataService;
    
    @Autowired
    public MetadataController(MetadataService metadataService) {
        this.metadataService = metadataService;
    }
    
    @PostMapping("/files")
    public ResponseEntity<FileMetadata> createFileMetadata(@RequestBody FileMetadata fileMetadata) {
        FileMetadata savedMetadata = metadataService.saveFileMetadata(fileMetadata);
        return new ResponseEntity<>(savedMetadata, HttpStatus.CREATED);
    }
    
    @GetMapping("/files/{id}")
    public ResponseEntity<FileMetadata> getFileMetadata(@PathVariable Long id) {
        return metadataService.getFileMetadataById(id)
                .map(metadata -> new ResponseEntity<>(metadata, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/users/{userId}/files")
    public ResponseEntity<List<FileMetadata>> getUserFiles(@PathVariable Long userId) {
        List<FileMetadata> files = metadataService.getAllFileMetadataByUserId(userId);
        return new ResponseEntity<>(files, HttpStatus.OK);
    }
    
    @DeleteMapping("/files/{id}")
    public ResponseEntity<Void> deleteFileMetadata(@PathVariable Long id) {
        metadataService.deleteFileMetadata(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    
    @GetMapping("/files/{fileId}/chunks")
    public ResponseEntity<List<ChunkMetadata>> getFileChunks(@PathVariable Long fileId) {
        List<ChunkMetadata> chunks = metadataService.getChunksByFileId(fileId);
        return new ResponseEntity<>(chunks, HttpStatus.OK);
    }
    
    @PostMapping("/chunks")
    public ResponseEntity<ChunkMetadata> createChunkMetadata(@RequestBody ChunkMetadata chunkMetadata) {
        ChunkMetadata savedChunk = metadataService.saveChunkMetadata(chunkMetadata);
        return new ResponseEntity<>(savedChunk, HttpStatus.CREATED);
    }
    
    @GetMapping("/chunks/{chunkId}")
    public ResponseEntity<ChunkMetadata> getChunkMetadata(@PathVariable String chunkId) {
        return metadataService.getChunkById(chunkId)
                .map(chunk -> new ResponseEntity<>(chunk, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @DeleteMapping("/chunks/{id}")
    public ResponseEntity<Void> deleteChunkMetadata(@PathVariable Long id) {
        metadataService.deleteChunkMetadata(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}