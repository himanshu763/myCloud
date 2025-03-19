┌─────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                           System - High Level Architecture                                  │
└─────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────┐          ┌─────────────────────────────────────┐          ┌────────────────────────────────────────┐
│            Client Layer             │          │            Gateway Layer            │          │         Service Layer                  │
│                                     │          │                                     │          │                                        │
│ ┌────────────────┐ ┌───────────────┐│          │  ┌───────────────────────────────┐  │          │  ┌──────────────────┐ ┌───────────────┐│
│ │ Web Interface  │ │Mobile Apps    ││          │  │                               │  │          │  │ Auth Service     │ │Metadata       ││
│ │                │ │               ││          │  │                               │  │          │  │ - Authentication │ │Service        ││
│ │ - React/Angular│ │- iOS/Android  ││    ┌─────┼─▶│   API Gateway Service         │  │    ┌─────┼─▶│ - User Management│ │- File Info    ││
│ │ - File Browser │ │- File Access  ││    │     │  │                               │  │    │     │  │ - Authorization  │ │- Search       ││
│ └────────────────┘ └───────────────┘│◄───┘     │  │ - Request Routing             │  │    │     │  └──────────────────┘ └───────────────┘│
│                                     │          │  │ - Authentication              │◄─┼────┘     │                                        │
│ ┌────────────────┐ ┌───────────────┐│          │  │ - Load Balancing              │  │    │     │  ┌──────────────────┐ ┌───────────────┐│
│ │Desktop Client  │ │ CLI Tool      ││◄─────────┼──│ - Rate Limiting               │◄─┼────┼─────┼─▶│Chunk Service     │ │Storage        ││
│ │                │ │               ││          │  │                               │  │    │     │  │ - File Chunking  │ │Provider       ││
│ │ - Sync Client  │ │- Admin Tools  ││          │  │                               │  │    └─────┼─▶│ - Distribution   │ │Integration    ││
│ │ - Direct Upload│ │- Batch Ops    ││          │  └───────────────────────────────┘  │          │  │ - Reassembly     │ │- Cloud/Local  ││
│ └────────────────┘ └───────────────┘│          │                                     │          │  └──────────────────┘ └───────────────┘│
└─────────────────────────────────────┘          └─────────────────────────────────────┘          └────────────────────────────────────────┘
                                                                                                                      │
                                                                                                                      │
┌─────────────────────────────────────┐          ┌─────────────────────────────────────┐                              │
│         Data Layer                  │          │       Infrastructure Layer          │◄─────────────────────────────┘
│                                     │          │                                     │
│  ┌───────────────┐ ┌───────────────┐│          │  ┌───────────────┐ ┌───────────────┐│
│  │ SQL Database  │ │NoSQL Database ││          │  │ Load Balancer │ │Service        ││
│  │               │ │               ││          │  │               │ │Discovery      ││
│  │ - User Data   │ │- Metadata     ││◄─────────┼──┤ - Traffic     │ │- Service      ││
│  │ - Auth Info   │ │- File Index   ││          │  │   Distribution│ │  Registry     ││
│  └───────────────┘ └───────────────┘│          │  └───────────────┘ └───────────────┘│
│                                     │          │                                     │
│  ┌───────────────┐ ┌───────────────┐│          │  ┌───────────────┐ ┌───────────────┐│
│  │Search Index   │ │File Chunks    ││          │  │ Config Server │ │Monitoring     ││
│  │               │ │               ││          │  │               │ │& Logging      ││
│  │ -Elasticsearch│ │- Distributed  ││          │  │ - Centralized │ │- Performance  ││
│  │ - Fast Queries│ │  Storage      ││          │  │  Configuration│ │  Metrics      ││
│  └───────────────┘ └───────────────┘│          │  └───────────────┘ └───────────────┘│
└─────────────────────────────────────┘          └─────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                   Service Components Detail                                              │
└──────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐
│ API Gateway Service    │     │ Auth Account Service   │     │ Metadata Service       │     │ Chunk Service          │
│                        │     │                        │     │                        │     │                        │
│ - SecurityConfig       │     │ - AuthController       │     │ - MetadataController   │     │ - ChunkController      │
│ - CorsConfig           │     │ - AuthService          │     │ - MetadataService      │     │ - ChunkService         │
│ - RouteConfiguration   │     │ - UserController       │     │ - SearchService        │     │ - DistributionService  │
│ - RequestFilter        │     │ - UserService          │     │ - TagRepository        │     │ - ChunkRepository      │
│ - ResponseFilter       │     │ - UserRepository       │     │ - VersionRepository    │     │ - StorageProviderClient│
│ - RateLimiter          │     │ - RoleRepository       │     │ - FileMetadataRepo     │     │ - ChunkProcessor       │
│                        │     │ - JwtTokenProvider     │     │                        │     │                        │
└────────────────────────┘     └────────────────────────┘     └────────────────────────┘     └────────────────────────┘

┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐
│ Storage Provider       │     │ Notification Service   │     │ Sharing Service        │     │ Analytics Service      │
│ Integration            │     │                        │     │                        │     │                        │
│ - S3Adapter            │     │ - NotificationManager  │     │ - ShareController      │     │ - UsageTracker         │
│ - AzureAdapter         │     │ - EmailService         │     │ - ShareService         │     │ - ActivityMonitor      │
│ - GoogleCloudAdapter   │     │ - PushNotificationSvc  │     │ - LinkGenerator        │     │ - StorageAnalytics     │
│ - LocalStorageAdapter  │     │ - WebSocketManager     │     │ - PermissionManager    │     │ - ReportGenerator      │
│ - ProviderFactory      │     │ - NotificationRepo     │     │ - ShareRepository      │     │ - DashboardService     │
│                        │     │                        │     │                        │     │                        │
└────────────────────────┘     └────────────────────────┘     └────────────────────────┘     └────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                   Key Domain Models                                                      │
└──────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐
│ User                   │     │ FileMetadata           │     │ Chunk                  │     │ StorageProvider        │
│                        │     │                        │     │                        │     │                        │
│ - id: String           │     │ - id: String           │     │ - id: String           │     │ - id: String           │
│ - username: String     │     │ - name: String         │     │ - fileId: String       │     │ - name: String         │
│ - email: String        │     │ - type: String         │     │ - sequenceNum: int     │     │ - type: ProviderType   │
│ - password: String     │     │ - size: long           │     │ - size: long           │     │ - credentials: Map     │
│ - roles: Set<Role>     │     │ - userId: String       │     │ - checksum: String     │     │ - endpoint: String     │
│ - enabled: boolean     │     │ - createdDate: Date    │     │ - storageLocation: Map │     │ - priority: int        │
│                        │     │ - tags: List<Tag>      │     │ - encrypted: boolean   │     │ - status: Status       │
└────────────────────────┘     └────────────────────────┘     └────────────────────────┘     └────────────────────────┘

┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐
│ Share                  │     │ Version                │     │ Notification           │     │ Analytics              │
│                        │     │                        │     │                        │     │                        │
│ - id: String           │     │ - id: String           │     │ - id: String           │     │ - userId: String       │
│ - fileId: String       │     │ - fileId: String       │     │ - userId: String       │     │ - storageUsed: long    │
│ - userId: String       │     │ - version: int         │     │ - type:NotificationType│     │ - fileCount: int       │
│ - sharedWith: String   │     │ - timestamp: Date      │     │ - message: String      │     │ - activityLog: List    │
│ - permission: Permission│    │ - changes: String      │     │ - read: boolean        │     │ - quotaLimit: long     │
│ - expiry: Date         │     │ - userId: String       │     │ - createdAt: Date      │     │ - lastUpdated: Date    │
│                        │     │                        │     │                        │     │                        │
└────────────────────────┘     └────────────────────────┘     └────────────────────────┘     └────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                       File Upload Workflow                                                           │
│                                                                                                                      │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌────────────┐      │
│  │  Client  │───►│   API    │───►│   Auth   │───►│  Chunk   │───►│ Storage  │───►│ Metadata │───►│Notification│      │
│  │          │    │ Gateway  │    │ Service  │    │ Service  │    │ Provider │    │ Service  │    │ Service    │      │
│  └──────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘    └────────────┘      │
│       │               │               │              │                │               │               │              │
│       │ Upload File   │               │              │                │               │               │              │
│       │───────────────►               │              │                │               │               │              │
│       │               │ Authenticate  │              │                │               │               │              │
│       │               │───────────────►              │                │               │               │              │
│       │               │ Token         │              │                │               │               │              │
│       │               │◄──────────────┘              │                │               │               │              │
│       │               │               │ Chunk File   │                │               │               │              │
│       │               │───────────────────────────────►               │               │               │              │
│       │               │               │              │ Store Chunks   │               │               │              │
│       │               │               │              │───────────────►│               │               │              │
│       │               │               │              │ Storage Confirm│               │               │              │
│       │               │               │              │◄───────────────┘               │               │              │
│       │               │               │              │ Create Metadata│               │               │              │
│       │               │               │              │───────────────────────────────►│               │              │
│       │               │               │              │              │                 │ Send          │              │
│       │               │               │              │              │                 │ Notification  │              │
│       │               │               │              │              │                 │──────────────►│              │
│       │               │ Upload        │              │              │                 │               │              │
│       │               │ Complete      │              │              │                 │               │              │
│       │◄──────────────────────────────────────────────────────────────────────────────────────────────┘              │
│                                                                                                                      │
└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┐
│                                       File Download Workflow                                                         │
│                                                                                                                      │
│  ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐        │
│  │  Client  │───►│   API    │───►│   Auth   │───►│ Metadata │───►│  Chunk   │───►│ Storage  │───►│Analytics │        │
│  │          │    │ Gateway  │    │ Service  │    │ Service  │    │ Service  │    │ Provider │    │ Service  │        │
│  └──────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘        │
│       │               │               │              │                │               │               │              │
│       │ Request File  │               │              │                │               │               │              │
│       │───────────────►               │              │                │               │               │              │
│       │               │ Authenticate  │              │                │               │               │              │
│       │               │───────────────►              │                │               │               │              │
│       │               │ Token         │              │                │               │               │              │
│       │               │◄──────────────┘              │                │               │               │              │
│       │               │               │ Get Metadata │                │               │               │              │
│       │               │───────────────────────────────►               │               │               │              │
│       │               │               │              │ Request Chunks │               │               │              │
│       │               │               │              │───────────────►│               │               │              │
│       │               │               │              │                │ Fetch Chunks  │               │              │
│       │               │               │              │                │──────────────►│               │              │
│       │               │               │              │                │ Return Chunks │               │              │
│       │               │               │              │                │◄──────────────┘               │              │
│       │               │               │              │                │ Reassemble    │               │              │
│       │               │               │              │                │ File          │               │              │
│       │               │               │              │                │───────────────────────────────►              │
│       │               │ Return File   │              │                │               │               │              │
│       │◄──────────────────────────────────────────────────────────────┘               │               │              │
│                                                                                                                      │
└──────────────────────────────────────────────────────────────────────────────────────────────────────────────────────┘