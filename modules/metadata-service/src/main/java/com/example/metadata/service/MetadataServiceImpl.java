package com.example.metadata.service;

import com.example.metadata.model.ChunkMetadata;
import com.example.metadata.model.FileMetadata;
import com.example.metadata.repository.ChunkMetadataRepository;
import com.example.metadata.repository.FileMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class MetadataServiceImpl implements MetadataService {
    
    private static final Logger logger = LoggerFactory.getLogger(MetadataServiceImpl.class);
    
    private final FileMetadataRepository fileMetadataRepository;
    private final ChunkMetadataRepository chunkMetadataRepository;
    
    @Autowired
    public MetadataServiceImpl(FileMetadataRepository fileMetadataRepository, 
                              ChunkMetadataRepository chunkMetadataRepository) {
        this.fileMetadataRepository = fileMetadataRepository;
        this.chunkMetadataRepository = chunkMetadataRepository;
    }
    
    @Override
    @Transactional
    public FileMetadata saveFileMetadata(FileMetadata fileMetadata) {
        logger.info("Saving file metadata: {}", fileMetadata.getFileName());
        return fileMetadataRepository.save(fileMetadata);
    }
    
    @Override
    @Cacheable(value = "fileMetadata", key = "#id")
    public Optional<FileMetadata> getFileMetadataById(Long id) {
        logger.info("Fetching file metadata for id: {}", id);
        return fileMetadataRepository.findById(id);
    }
    
    @Override
    @Cacheable(value = "fileMetadataByUser", key = "#id + '-' + #userId")
    public Optional<FileMetadata> getFileMetadataByIdAndUserId(Long id, Long userId) {
        logger.info("Fetching file metadata for id: {} and userId: {}", id, userId);
        return fileMetadataRepository.findByIdAndUserId(id, userId);
    }
    
    @Override
    @Cacheable(value = "userFiles", key = "#userId")
    public List<FileMetadata> getAllFileMetadataByUserId(Long userId) {
        logger.info("Fetching all file metadata for userId: {}", userId);
        return fileMetadataRepository.findByUserId(userId);
    }
    
    @Override
    @CacheEvict(value = {"fileMetadata", "fileMetadataByUser", "userFiles"}, allEntries = true)
    @Transactional
    public void deleteFileMetadata(Long id) {
        logger.info("Deleting file metadata for id: {}", id);
        fileMetadataRepository.deleteById(id);
    }
    
    @Override
    @Cacheable(value = "fileChunks", key = "#fileId")
    public List<ChunkMetadata> getChunksByFileId(Long fileId) {
        logger.info("Fetching chunks for fileId: {}", fileId);
        return chunkMetadataRepository.findByFileId(fileId);
    }
    
    @Override
    @Transactional
    public ChunkMetadata saveChunkMetadata(ChunkMetadata chunkMetadata) {
        logger.info("Saving chunk metadata: {}", chunkMetadata.getChunkId());
        return chunkMetadataRepository.save(chunkMetadata);
    }
    
    @Override
    @Cacheable(value = "chunkMetadata", key = "#chunkId")
    public Optional<ChunkMetadata> getChunkById(String chunkId) {
        logger.info("Fetching chunk metadata for chunkId: {}", chunkId);
        return chunkMetadataRepository.findByChunkId(chunkId);
    }
    
    @Override
    @CacheEvict(value = {"chunkMetadata", "fileChunks"}, allEntries = true)
    @Transactional
    public void deleteChunkMetadata(Long id) {
        logger.info("Deleting chunk metadata for id: {}", id);
        chunkMetadataRepository.deleteById(id);
    }
}