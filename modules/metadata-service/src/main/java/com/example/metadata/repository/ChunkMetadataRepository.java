package com.example.metadata.repository;

import com.example.metadata.model.ChunkMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChunkMetadataRepository extends JpaRepository<ChunkMetadata, Long> {
    List<ChunkMetadata> findByFileId(Long fileId);
    Optional<ChunkMetadata> findByChunkId(String chunkId);
}