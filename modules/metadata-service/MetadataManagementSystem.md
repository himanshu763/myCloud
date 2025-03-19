┌───────────────────────────────────────────────────────────────────────────────────────────┐
│                                Metadata Service Architecture                              │
└───────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────┐     ┌────────────────────────────┐     ┌─────────────────────────┐
│       API Gateway           │     │   MetadataController       │     │    MetadataService      │
│                             │     │                            │     │    <<Interface>>        │
│ - Request Routing           │◄────┤ - getFileMetadata()        │◄────┤ + getFileMetadata()     │
│ - Authorization             │     │ - updateMetadata()         │     │ + updateMetadata()      │
│ - Load Balancing            │     │ - searchFiles()            │     │ + searchFiles()         │
│                             │     │ - addTags()                │     │ + addTags()             │
│                             │     │ - getVersionHistory()      │     │ + getVersionHistory()   │
└─────────────────────────────┘     └────────────────────────────┘     └─────────────────────────┘
                                                                                  ▲
                                                                                  │
                                                                                  │ implements
                                                                                  │
┌────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                  MetadataServiceImpl                                           │
│                                                                                                │
│ - fileMetadataRepository: FileMetadataRepository                                               │
│ - tagRepository: TagRepository                                                                 │
│ - versionRepository: VersionRepository                                                         │
│ - searchService: SearchService                                                                 │
│ - chunkService: ChunkService                                                                   │
│                                                                                                │
│ + getFileMetadata(String fileId, String userId)                                                │
│ + updateMetadata(FileMetadata metadata, String userId)                                         │
│ + searchFiles(SearchCriteria criteria, String userId)                                          │
│ + addTags(String fileId, List<String> tags, String userId)                                     │
│ + getVersionHistory(String fileId, String userId)                                              │
│ - validateOwnership(String fileId, String userId)                                              │
│ - indexMetadataForSearch(FileMetadata metadata)                                                │
└────────────────────────────────────────────────────────────────────────────────────────────────┘
                         │                                                ▲
                         │ uses                                           │ uses
                         ▼                                                │
┌─────────────────────────────────┐                ┌───────────────────────────────────────────┐
│   FileMetadataRepository        │                │             SearchService                 │
│                                 │                │                                           │
│ + findById(String id)           │                │ + indexDocument(FileMetadata metadata)    │
│ + findByUserId(String userId)   │◄──────uses────►│ + search(SearchCriteria criteria)         │
│ + save(FileMetadata metadata)   │                │ + deleteIndex(String fileId)              │
│ + delete(String id)             │                │ + updateIndex(FileMetadata metadata)      │
└─────────────────────────────────┘                └───────────────────────────────────────────┘

┌─────────────────────────────────┐     ┌────────────────────────────┐     ┌─────────────────────────┐
│        TagRepository            │     │     VersionRepository      │     │    ChunkService         │
│                                 │     │                            │     │                         │
│ + findByFileId(String fileId)   │     │ + findByFileId(String id)  │     │ + getChunkInfo()        │
│ + saveAll(List<Tag> tags)       │     │ + save(Version version)    │     │ + validateFileExists()  │
│ + deleteByFileId(String fileId) │     │ + deleteByFileId(String id)│     │                         │
└─────────────────────────────────┘     └────────────────────────────┘     └─────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                      Domain Models                                                  │
│                                                                                                     │
│  ┌──────────────────────┐     ┌─────────────────────┐    ┌──────────────────┐    ┌────────────────┐ │
│  │    FileMetadata      │     │        Tag          │    │     Version      │    │ SearchCriteria │ │
│  │                      │     │                     │    │                  │    │                │ │
│  │ - id: String         │◄────┤ - id: String        │    │ - id: String     │    │ - query: String│ │
│  │ - name: String       │ 1:n │ - name: String      │    │ - fileId: String │    │ - tags: List   │ │
│  │ - type: String       │     │ - fileId: String    │    │ - version: int   │    │ - type: String │ │
│  │ - size: long         │     └─────────────────────┘    │ - timestamp: Date│    │ - dateRange    │ │
│  │ - userId: String     │                                │ - userId: String │    │ - sizeRange    │ │ 
│  │ - createdDate: Date  │     ┌─────────────────────┐    │ - changes: String│    │ - sortBy       │ │
│  │ - modifiedDate: Date │◄────┤   FileLocation      │    └──────────────────┘    └────────────────┘ │
│  │ - description: String│ 1:1 │                     │                                               │
│  │ - starred: boolean   │     │ - fileId: String    │                                               │
│  │ - shared: boolean    │     │ - chunkIds: List    │                                               │
│  │ - parentFolderId     │     │ - providerMap: Map  │                                               │
│  │ - tags: List<Tag>    │     │                     │                                               │
│  │ - versions: List<Ver>│     └─────────────────────┘                                               │
│  └──────────────────────┘                                                                           │
└─────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                             Metadata Search & Indexing Flow                                         │
│                                                                                                     │
│  ┌────────────┐     ┌────────────────┐     ┌─────────────────┐     ┌────────────────┐               │
│  │  Client    │────►│ MetadataService│────►│  SearchService  │────►│  Search Index  │               │
│  └────────────┘     └────────────────┘     └─────────────────┘     └────────────────┘               │
│       ▲                      │                                            │                         │
│       │                      │                                            │                         │
│       │                      │                                            │                         │
│       │                      ▼                                            │                         │
│       │              ┌────────────────┐                                   │                         │
│       │              │ FileMetadata   │◄──────────────────────────────────┘                         │
│       │              │ Repository     │                                                             │
│       │              └────────────────┘                                                             │
│       │                      │                                                                      │
│       │                      │                                                                      │
│       └──────────────────────┘                                                                      │
│                                                                                                     │
└─────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                               File Version Management Flow                                          │
│                                                                                                     │
│  ┌────────────┐      ┌────────────────┐     ┌─────────────────┐      ┌────────────────┐             │
│  │  Client    │─────►│ MetadataService│────►│VersionRepository│◄────►│  ChunkService  │             │
│  └────────────┘Update└────────────────┘     └─────────────────┘      └────────────────┘             │
│       ▲         File              │                    │                     │                      │
│       │                           │                    │                     │                      │
│       │                           ▼                    │                     │                      │
│       │                   ┌────────────────┐           │                     │                      │
│       │                   │ Create Version │           │                     │                      │
│       │                   └────────────────┘           │                     │                      │
│       │                           │                    │                     │                      │
│       │                           ▼                    │                     │                      │
│       │                   ┌────────────────┐           │                     │                      │
│       │                   │Update Metadata │◄──────────┘                     │                      │
│       │                   └────────────────┘                                 │                      │
│       │                           │                                          │                      │
│       │                           ▼                                          │                      │
│       │                   ┌────────────────┐                                 │                      │
│       └───────────────────┤Return Response │◄────────────────────────────────┘                      │
│                           └────────────────┘                                                        │
└─────────────────────────────────────────────────────────────────────────────────────────────────────┘