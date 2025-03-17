package com.example.metadata.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "chunks")
public class ChunkMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String chunkId;
    private Integer chunkNumber;
    private Long size;
    private String checksum;
    private String providerType;
    private String providerFileId;
    private String providerLocation;
    private LocalDateTime uploadDate;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "file_id")
    private FileMetadata file;
    
    // Getters and setters
    // ...
}