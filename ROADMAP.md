    User (via React UI)
             │
             ▼
    ┌─────────────────────────┐
    │     API Gateway         │ (Optional, Spring Cloud Gateway)
    └─────────────┬───────────┘
                  │
                  ├───────────┐
                  │           │
                  ▼           ▼
    ┌─────────────────┐  ┌─────────────────────┐
    │Auth & Account   │  │ Chunk & Cloud Conn. │
    │ Service         │  │ Service             │
    │ (Spring Boot)   │  │ (Spring Boot)       │
    └────────┬────────┘  └───────────┬─────────┘
             │                       │
             ▼                       ▼
    ┌─────────────────────────────────────────┐
    │     Metadata Management Service         │
    │(Spring Boot + PostgreSQL + Redis Cache) │
    └──────────────────┬──────────────────────┘
                       ▼
         ┌───────────────────────────┐
         │  PostgreSQL (Persistent)  │
         │  Redis (Fast Cache)       │
         └───────────────────────────┘
    
    
    📍 Detailed Explanation (Microservices):
    🔒 1. Auth & Account Service
    Tasks:
    User registration, login, and JWT token management.
    Handles OAuth to link cloud provider accounts.
    Data stored: User details, linked email accounts, OAuth tokens.
    Technology: Spring Boot, JWT, PostgreSQL
    
    
    Auth & Account Service
    ├── AuthController
    ├── UserService
    ├── OAuthService
    ├── UserRepository
    └── JWTTokenProvider
    
    
    2. Chunk & Cloud Connector Service
    Responsibilities:
    Receives files, splits into chunks.
    Communicates with cloud providers (Google Drive, Mega, Dropbox, Jio Cloud).
    Uploads/downloads chunks.
    Technology: Spring Boot, Cloud Provider APIs
    
    
    Chunk & Cloud Connector Service
    ├── FileController
    ├── ChunkService
    ├── CloudProviderClient (interface)
    │    ├─ GoogleDriveClient
    │    ├─ DropboxClient
    │    ├─ MegaClient
    │    └─ JioClient
    └── StoragePolicy (optional)
    
    
    3. Metadata Management Service
    Handles metadata storage and retrieval.
    Stores metadata persistently in PostgreSQL, caches frequently accessed metadata in Redis for faster access.
    Provides metadata lookup to other services.
    
    
    Metadata Management Service
    ├── MetadataController
    ├── MetadataService
    ├── PostgreSQLRepository
    └── RedisCacheClient


Project Structure 

        distributed-storage-backend/
        │
        ├── auth-account-service
        │   ├── src/main/java/com/example/auth/controller
        │   ├── src/main/java/com/example/auth/service
        │   ├── src/main/java/com/example/auth/repository
        │   ├── src/main/java/com/example/auth/model
        │   └── src/main/resources
        │   └── src/test/java/com/example/auth
        │   └── build.gradle
        │
        ├── chunk-cloud-service
        │   ├── src/main/java/com/example/chunk/controller
        │   ├── src/main/java/com/example/chunk/service
        │   ├── src/main/java/com/example/chunk/provider
        │   ├── src/main/java/com/example/chunk/model
        │   └── src/main/resources
        │   └── src/test/java/com/example/chunk
        │   └── build.gradle
        │
        ├── metadata-service
        │   ├── src/main/java/com/example/metadata/controller
        │   ├── src/main/java/com/example/metadata/service
        │   ├── src/main/java/com/example/metadata/repository
        │   ├── src/main/java/com/example/metadata/cache
        │   └── src/main/resources
        │   └── src/test/java/com/example/metadata
        │   └── build.gradle
        │
        ├── api-gateway
        │   ├── src/main/java/com/example/gateway
        │   ├── src/main/resources
        │   ├── src/main/resources/config
        │   └── src/test/java/com/example/gateway
        │   └── build.gradle
        │
        └── settings.gradle
