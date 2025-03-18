package com.example.chunk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Chunk {
    private String id;
    private byte[] data;
    private int sequenceNumber;
    private String fileId;
    private String cloudProviderId;
    private String providerType;
    private String cloudFileId;
}