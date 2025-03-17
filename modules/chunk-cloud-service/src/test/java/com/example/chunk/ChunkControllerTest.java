package com.example.chunk;

import com.example.chunk.controller.ChunkController;
import com.example.chunk.model.File;
import com.example.chunk.service.ChunkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ChunkController.class)
public class ChunkControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private ChunkService chunkService;
    
    @Test
    public void testFileUpload() throws Exception {
        // Prepare test data
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.txt",
                "text/plain",
                "Hello, World!".getBytes()
        );
        
        // Mock service
        File mockFile = new File();
        mockFile.setId("test-id");
        mockFile.setName("test.txt");
        mockFile.setSize(13L);
        mockFile.setCreatedAt(LocalDateTime.now());
        mockFile.setChunks(new ArrayList<>());
        
        when(chunkService.uploadFile(any(), anyString(), any()))
                .thenReturn(mockFile);
        
        // Perform request
        mockMvc.perform(multipart("/api/chunks/upload")
                        .file(file)
                        .param("userId", "user1")
                        .param("providers", "GOOGLE_DRIVE"))
                .andExpect(status().isOk());
    }
}