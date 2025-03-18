package com.example.chunk.service;

import com.example.chunk.model.Chunk;
import com.example.chunk.model.ChunkUploadStatus;
import com.example.chunk.model.File;
import com.example.chunk.provider.CloudProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChunkServiceImpl implements ChunkService {
    
    private final Map<String, CloudProvider> providerMap;
    
    @Autowired
    public ChunkServiceImpl(List<CloudProvider> providers) {
        this.providerMap = providers.stream()
                .collect(Collectors.toMap(CloudProvider::getProviderName, provider -> provider));
    }
    
    @Override
    public File uploadFile(MultipartFile multipartFile, String userId, List<String> providers) throws IOException {
        // Validate providers
        validateProviders(providers);
        
        // Create file metadata
        File file = new File();
        file.setId(UUID.randomUUID().toString());
        file.setName(multipartFile.getOriginalFilename());
        file.setSize(multipartFile.getSize());
        file.setMimeType(multipartFile.getContentType());
        file.setUserId(userId);
        file.setCreatedAt(LocalDateTime.now());
        
        // Split file into chunks
        List<Chunk> chunks = splitFile(multipartFile.getBytes(), file.getId());
        file.setChunks(chunks);
        
        // Distribute and upload chunks
        uploadChunks(chunks, providers);
        
        // TODO: Send metadata to Metadata Management Service
        
        return file;
    }
    
    @Override
    public byte[] downloadFile(String fileId, String userId) {
        // TODO: Get file metadata from Metadata Management Service
        // Placeholder implementation
        return new byte[0];
    }
    
    @Override
    public boolean deleteFile(String fileId, String userId) {
        // TODO: Implement file deletion
        return false;
    }
    
    @Override
    public List<File> getUserFiles(String userId) {
        // TODO: Get user files from Metadata Management Service
        return Collections.emptyList();
    }
    
    private List<Chunk> splitFile(byte[] fileData, String fileId) {
        List<Chunk> chunks = new ArrayList<>();
        int chunkSize = 5 * 1024 * 1024; // 5MB chunks
        
        int chunksCount = (int) Math.ceil(fileData.length / (double) chunkSize);
        
        for (int i = 0; i < chunksCount; i++) {
            int start = i * chunkSize;
            int length = Math.min(chunkSize, fileData.length - start);
            
            byte[] chunkData = Arrays.copyOfRange(fileData, start, start + length);
            
            Chunk chunk = new Chunk();
            chunk.setId(UUID.randomUUID().toString());
            chunk.setData(chunkData);
            chunk.setSequenceNumber(i);
            chunk.setFileId(fileId);
            
            chunks.add(chunk);
        }
        
        return chunks;
    }
    
    private void uploadChunks(List<Chunk> chunks, List<String> providers) {
        int providerIndex = 0;
        
        for (Chunk chunk : chunks) {
            String providerName = providers.get(providerIndex);
            CloudProvider provider = providerMap.get(providerName);
            
            if (provider != null) {
                chunk.setProviderType(providerName);
                
                ChunkUploadStatus status = provider.uploadChunk(chunk);
                if (status.isSuccess()) {
                    chunk.setCloudFileId(status.getCloudFileId());
                    chunk.setCloudProviderId(providerName);
                }
                // TODO: Handle upload failure
            }
            
            // Round-robin distribution
            providerIndex = (providerIndex + 1) % providers.size();
        }
    }
    
    private void validateProviders(List<String> providers) {
        for (String provider : providers) {
            if (!providerMap.containsKey(provider)) {
                throw new IllegalArgumentException("Unsupported provider: " + provider);
            }
        }
    }
}