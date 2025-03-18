package com.example.metadata.service;

import com.example.metadata.model.ChunkMetadata;
import com.example.metadata.model.FileMetadata;
import java.util.List;
import java.util.Optional;

public interface MetadataService {
    FileMetadata saveFileMetadata(FileMetadata fileMetadata);
    Optional<FileMetadata> getFileMetadataById(Long id);
    Optional<FileMetadata> getFileMetadataByIdAndUserId(Long id, Long userId);
    List<FileMetadata> getAllFileMetadataByUserId(Long userId);
    void deleteFileMetadata(Long id);
    
    List<ChunkMetadata> getChunksByFileId(Long fileId);
    ChunkMetadata saveChunkMetadata(ChunkMetadata chunkMetadata);
    Optional<ChunkMetadata> getChunkById(String chunkId);
    void deleteChunkMetadata(Long id);
}