┌───────────────────────────────────────────────────────────────────────────────────────────┐
│                                  API Gateway Service Architecture                         │
└───────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────┐     ┌────────────────────────────┐     ┌─────────────────────────┐
│       Client                │     │     API Gateway            │     │   Auth Service          │
│ (Web/Mobile/Desktop)        │     │                            │     │                         │
│                             │     │ - Authentication           │     │ - User Authentication   │
│ - REST API Requests         │◄────┤ - Request Routing          │◄────┤ - Token Validation      │
│ - File Upload/Download      │     │ - Load Balancing           │     │ - User Management       │
│                             │     │ - Circuit Breaking         │     │                         │
└─────────────────────────────┘     └────────────────────────────┘     └─────────────────────────┘
                                                  │
                                                  │
                   ┌──────────────────────────────┼──────────────────────────────────┐
                   │                              │                                  │
                   ▼                              ▼                                  ▼
┌─────────────────────────────┐     ┌────────────────────────────┐     ┌─────────────────────────┐
│   Chunk Cloud Service       │     │     Other Microservices    │     │   Monitoring Service    │
│                             │     │                            │     │                         │
│ - File Management           │     │ - Additional Business      │     │ - Performance Metrics   │
│ - Chunk Distribution        │     │   Logic Services           │     │ - Health Checks         │
│                             │     │                            │     │ - Alerting              │
└─────────────────────────────┘     └────────────────────────────┘     └─────────────────────────┘

                                  API Gateway Components
┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                                                                                             │
│  ┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐   │
│  │  SecurityConfig        │     │      CorsConfig        │     │   RouteConfiguration   │   │
│  │                        │     │                        │     │                        │   │
│  │ - Security filters     │     │ - Allowed origins      │     │ - Service routes       │   │
│  │ - Authentication config│     │ - Allowed methods      │     │ - Load balancing rules │   │
│  │ - Authorization rules  │     │ - Allowed headers      │     │ - Circuit breakers     │   │
│  └────────────────────────┘     └────────────────────────┘     └────────────────────────┘   │
│                                                                                             │
│  ┌────────────────────────┐     ┌────────────────────────┐     ┌────────────────────────┐   │
│  │   RequestFilter        │     │   ResponseFilter       │     │      RateLimiter       │   │
│  │                        │     │                        │     │                        │   │
│  │ - Request validation   │     │ - Response modification│     │ - API rate limiting    │   │
│  │ - Header enrichment    │     │ - Error handling       │     │ - Quota management     │   │
│  │ - Logging              │     │ - Compression          │     │ - Throttling rules     │   │
│  └────────────────────────┘     └────────────────────────┘     └────────────────────────┘   │
│                                                                                             │
└─────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────┐
│                             Authentication Flow                                             │
│                                                                                             │
│  ┌────────────┐          ┌────────────┐          ┌────────────┐          ┌────────────┐     │
│  │  Client    │──Login──►│API Gateway │──Auth───►│Auth Service│◄──Verify─┤  Database  │     │
│  └────────────┘          └────────────┘  Request └────────────┘  User    └────────────┘     │
│        │                       │                       │                                    │
│        │                       │                       │                                    │
│        │                       │◄─── JWT Token ────────┘                                    │
│        │                       │                                                            │
│        │◄──── JWT Token ───────┘                                                            │
│        │                                                                                    │
│        │  ┌────────────┐          ┌────────────┐           ┌────────────┐                   │
│        └──┤API Gateway │──Token──►│  Service   │──Process─►│  Response  │─────┐             │
│           └────────────┘Validation│            │  Request  └────────────┘     │             │
│                                   └────────────┘                              │             │
│                                                                               │             │
│                                  ┌────────────┐                               │             │
│                                  │  Client    │◄───────────Response───────────┘             │
│                                  └────────────┘                                             │
└─────────────────────────────────────────────────────────────────────────────────────────────┘