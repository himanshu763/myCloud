package com.example.metadata.repository;

import com.example.metadata.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {
    List<FileMetadata> findByUserId(Long userId);
    Optional<FileMetadata> findByIdAndUserId(Long id, Long userId);
    Optional<FileMetadata> findByFileNameAndUserId(String fileName, Long userId);
}