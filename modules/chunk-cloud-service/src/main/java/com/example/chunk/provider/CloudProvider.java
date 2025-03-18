package com.example.chunk.provider;

import com.example.chunk.model.Chunk;
import com.example.chunk.model.ChunkUploadStatus;

public interface CloudProvider {
    ChunkUploadStatus uploadChunk(Chunk chunk);
    byte[] downloadChunk(String cloudFileId);
    boolean deleteChunk(String cloudFileId);
    boolean isAuthenticated();
    String getProviderName();
}