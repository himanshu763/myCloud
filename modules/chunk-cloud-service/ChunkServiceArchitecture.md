┌───────────────────────────────────────────────────────────────────────────────────────────┐
│                                 Chunk Cloud Service Architecture                          │
└───────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────┐     ┌───────────────────────────┐     ┌─────────────────────────────┐
│    API Gateway      │     │   ChunkController         │     │      ChunkService           │
│                     │     │                           │     │   <<Interface>>             │
│  - Authentication   │◄────┤ - uploadFile()            │◄────┤ + uploadFile()              │
│  - Request Routing  │     │ - downloadFile()          │     │ + downloadFile()            │
│  - Load Balancing   │     │ - deleteFile()            │     │ + deleteFile()              │
│                     │     │ - getUserFiles()          │     │ + getUserFiles()            │
└─────────────────────┘     └───────────────────────────┘     └─────────────────────────────┘
                                                                           ▲
                                                                           │
                                                                           │ implements
                                                                           │
┌────────────────────────────────────────────────────────────────────────────────────────────┐
│                                  ChunkServiceImpl                                          │
│                                                                                            │
│ - Map<String, CloudProvider> providerMap                                                   │
│                                                                                            │
│ + ChunkServiceImpl(List<CloudProvider> providers)                                          │
│ + uploadFile(MultipartFile file, String userId, List<String> providers)                    │
│ + downloadFile(String fileId, String userId)                                               │
│ + deleteFile(String fileId, String userId)                                                 │
│ + getUserFiles(String userId)                                                              │
│ - splitFile(byte[] fileData, String fileId)                                                │
│ - uploadChunks(List<Chunk> chunks, List<String> providers)                                 │
│ - validateProviders(List<String> providers)                                                │
└────────────────────────────────────────────────────────────────────────────────────────────┘
                         │                                          ▲
                         │ uses                                     │ injects
                         ▼                                          │
┌─────────────────────────────┐                ┌───────────────────────────────────────┐
│  CloudProvider              │                │        CloudProviderFactory           │
│  <<Interface>>              │◄───creates─────┤                                       │
│                             │                │ + createProvider(ProviderType type)   │
│ + uploadChunk()             │                └───────────────────────────────────────┘
│ + downloadChunk()           │
│ + deleteChunk()             │
│ + getProviderName()         │
└─────────────────────────────┘
               ▲
               │ implements
      ┌────────┴───────────┐
      │                    │
┌─────────────────┐  ┌───────────────────┐  ┌───────────────────────┐
│ DropboxProvider │  │GoogleDriveProvider│  │  Other Providers...   │
│                 │  │                   │  │                       │
└─────────────────┘  └───────────────────┘  └───────────────────────┘

┌────────────────────────────────┐     ┌────────────────────────┐     ┌─────────────────────────┐
│            File                │     │         Chunk          │     │   ChunkUploadStatus     │
│                                │     │                        │     │                         │
│ - id: String                   │     │ - id: String           │     │ - SUCCESS               │
│ - name: String                 │     │ - fileId: String       │     │ - FAILED                │
│ - userId: String               │◄────┤ - sequence: int        │     │ - PENDING               │
│ - size: long                   │     │ - data: byte[]         │     └─────────────────────────┘
│ - createdAt: LocalDateTime     │  1:n│ - provider: String     │               ▲
│ - chunks: List<Chunk>          │     │ - status: UploadStatus │               │
└────────────────────────────────┘     └────────────────────────┘───────────────┘