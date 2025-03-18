package com.example.chunk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class File {
    private String id;
    private String name;
    private long size;
    private String mimeType;
    private String userId;
    private LocalDateTime createdAt;
    private List<Chunk> chunks;
}