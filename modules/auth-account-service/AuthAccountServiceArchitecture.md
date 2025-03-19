┌───────────────────────────────────────────────────────────────────────────────────────────┐
│                              Auth Account Service Architecture                            │
└───────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────┐     ┌────────────────────────────┐     ┌─────────────────────────┐
│       API Gateway           │     │      AuthController        │     │      AuthService        │
│                             │     │                            │     │                         │
│ - Authentication Requests   │◄────┤ - register()               │◄────┤ - register()            │
│ - Token Validation          │     │ - login()                  │     │ - login()               │
│ - Request Routing           │     │ - refreshToken()           │     │ - refreshToken()        │
│                             │     │ - validateToken()          │     │ - validateToken()       │
└─────────────────────────────┘     └────────────────────────────┘     └─────────────────────────┘
                                                                                   │
                                                                                   │ uses
                                                                                   ▼
┌─────────────────────────────┐     ┌────────────────────────────┐     ┌─────────────────────────┐
│      UserController         │     │       UserService          │     │    UserRepository       │
│                             │     │                            │     │                         │
│ - getUser()                 │◄────┤ - getUser()                │◄────┤ - findById()            │
│ - updateUser()              │     │ - updateUser()             │     │ - findByUsername()      │
│ - deleteUser()              │     │ - deleteUser()             │     │ - findByEmail()         │
│ - changePassword()          │     │ - changePassword()         │     │ - save()                │
│                             │     │ - assignRole()             │     │ - delete()              │
└─────────────────────────────┘     └────────────────────────────┘     └─────────────────────────┘
                                                                                   │
                                                                                   │ uses
                                                                                   ▼
┌────────────────────────────────────────────────────────┐     ┌─────────────────────────────────┐
│                     User                               │     │           Role                  │
│                                                        │     │                                 │
│ - id: String                                           │     │ - id: String                    │
│ - username: String                                     │     │ - name: String                  │
│ - email: String                                        │◄────┤ - description: String           │
│ - password: String (encrypted)                         │ n:m │ - permissions: List<Permission> │
│ - firstName: String                                    │     │                                 │
│ - lastName: String                                     │     │                                 │
│ - roles: Set<Role>                                     │     │                                 │
│ - enabled: boolean                                     │     │                                 │
│ - accountNonLocked: boolean                            │     │                                 │
│ - createdAt: LocalDateTime                             │     │                                 │
│ - lastLogin: LocalDateTime                             │     │                                 │
└────────────────────────────────────────────────────────┘     └─────────────────────────────────┘
                                                                                   │
                                                                                   │ uses
                                                                                   ▼
┌─────────────────────────────┐     ┌────────────────────────────────────────────────────────────┐
│      RoleRepository         │     │                   SecurityConfig                           │
│                             │     │                                                            │
│ - findByName()              │     │ - passwordEncoder: PasswordEncoder                         │
│ - findById()                │     │ - jwtTokenProvider: JwtTokenProvider                       │
│ - save()                    │     │ - userDetailsService: UserDetailsService                   │
│ - delete()                  │     │ + configure(HttpSecurity http)                             │
│                             │     │ + configureGlobal(AuthenticationManagerBuilder auth)       │
└─────────────────────────────┘     └────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────┐
│                              JWT Authentication Flow                                            │
│                                                                                                 │
│  ┌───────────┐          ┌───────────┐          ┌───────────┐          ┌───────────┐             │
│  │  Client   │──Login──►│API Gateway│──Auth───►│AuthService│◄──Verify─┤UserService│             │
│  └───────────┘          └───────────┘  Request └───────────┘    User  └───────────┘             │
│        │                       │                      │                     │                   │
│        │                       │                      │                     │                   │
│        │                       │◄── Generate JWT ─────┘                     │                   │
│        │                       │     Token                                  │                   │
│        │◄─── Return JWT ───────┘                                            │                   │
│        │      Token                                                         │                   │
│        │                                                                    │                   │
│        │                       ┌───────────┐          ┌───────────┐         │                   │
│        └───Subsequent ────────►│API Gateway│──Token──►│AuthService│─Validate Token────┐         │
│            Requests            └───────────┘Validation└───────────┘                   │         │
│                                      │                                                │         │
│                                      │◄───────────────Valid/Invalid Token─────────────┘         │
│                                      │                                                          │
│                                      └───►If valid, route to requested service                  │
│                                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────────────────────────────────┐
│                        Password Recovery & Account Management Flow                              │
│                                                                                                 │
│  ┌───────────┐      ┌───────────┐     ┌──────────────┐      ┌────────────┐     ┌────────────┐   │
│  │  Client   │◄────►│API Gateway│◄───►│UserController│◄────►│UserService │◄───►│EmailService│   │
│  └───────────┘      └───────────┘     └──────────────┘      └────────────┘     └────────────┘   │
│                                               │                   │                             │
│                                               │                   │                             │
│                                               ▼                   ▼                             │
│                                        ┌──────────────┐      ┌────────────┐                     │
│                                        │UserRepository│◄────►│ Database   │                     │
│                                        └──────────────┘      └────────────┘                     │
│                                                                                                 │
└─────────────────────────────────────────────────────────────────────────────────────────────────┘