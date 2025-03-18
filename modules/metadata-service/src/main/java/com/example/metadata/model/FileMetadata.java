package com.example.metadata.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "files")
public class FileMetadata {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String fileName;
    private String originalFileName;
    private String contentType;
    private Long size;
    private String checksum;
    private LocalDateTime uploadDate;
    private LocalDateTime lastModified;
    
    @Column(name = "user_id")
    private Long userId;
    
    @OneToMany(mappedBy = "file", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ChunkMetadata> chunks;
    
    // Getters and setters
    // ...
}