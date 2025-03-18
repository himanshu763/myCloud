package com.example.chunk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChunkUploadStatus {
    private String chunkId;
    private boolean success;
    private String cloudFileId;
    private String errorMessage;
}