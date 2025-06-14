```markdown
# EDCS (Entity Data and Configuration System)

## Introduction
EDCS is a comprehensive system that integrates various programming languages and technologies, including Java, Python, and C++. It mainly focuses on object registration, management, and communication using gRPC and Protocol Buffers. The system provides a registry server to manage objects, and multiple clients (Java, Python, and C++) to interact with the server.

## Features
- **Multi - language Support**: Supports Java, Python, and C++ clients, allowing developers to choose the most suitable language for their needs.
- **Object Management**: Provides functions such as object registration, deregistration, update, and lookup.
- **Heartbeat Mechanism**: Ensures the availability of registered objects through a heartbeat mechanism with TTL (Time - To - Live) management.
- **Persistent Storage**: Uses SQLite to store object information, ensuring data persistence.

## Directory Structure
```
EDCS/
├── cpp - client/         # C++ client code
│   ├── CMakeLists.txt    # CMake configuration file
│   ├── main.cpp          # Main C++ client code
│   └── object_repository.proto # Protocol Buffers definition for C++
├── java - client/        # Java client code
│   ├── build.gradle      # Gradle build configuration
│   ├── gradle - 8.6/     # Gradle related files
│   │   ├── init.d/       # Gradle init scripts directory
│   │   │   └── readme.txt
│   │   └── LICENSE       # Gradle license file
│   ├── gradlew.bat       # Gradle wrapper script for Windows
│   └── src/
│       └── main/
│           └── proto/
│               └── object_repository.proto # Protocol Buffers definition for Java
├── proto/                # Protocol Buffers definitions
│   └── object_repository.proto
├── .vscode/              # VSCode configuration files
│   ├── launch.json       # Debug launch configuration
│   └── c_cpp_properties.json # C/C++ properties configuration
├── object_repository_pb2_grpc.py # Generated gRPC Python code
├── registry_server.py    # Registry server implementation in Python
└── test_client.py        # Python test client
```

## Prerequisites
### Java
- Java Development Kit (JDK) 8 or higher
- Gradle 8.6

### Python
- Python 3.6 or higher
- `grpcio` and `grpcio - tools`
- `sqlite3` (usually included in Python standard library)

### C++
- CMake 3.22 or higher
- gRPC and Protobuf libraries installed via vcpkg

## How to Build and Run

### Java Client
1. Navigate to the `java - client` directory.
2. Run `./gradlew build` to build the project.
3. Run the main class `org.example.demo.RegistryDemo` to start the client.

### Python Client and Server
#### Server
1. Navigate to the root directory of the project.
2. Run `python registry_server.py` to start the registry server.

#### Client
1. Navigate to the root directory of the project.
2. Run `python test_client.py` to start the Python test client.

### C++ Client
1. Navigate to the `cpp - client` directory.
2. Create a build directory, e.g., `mkdir build && cd build`.
3. Run `cmake..` to generate build files.
4. Run `cmake --build.` to build the project.
5. Run the generated executable `client` to start the C++ client.

## Protocol Buffers Definition
The `object_repository.proto` file defines the messages and services used in the system. It includes messages such as `RegisterRequest`, `RegisterResponse`, etc., and a service `ObjectRepository` with methods like `RegisterObject`, `DeregisterObject`, etc.

## License
The project uses the Apache License 2.0. See the `java - client/gradle - 8.6/LICENSE` file for more details.

## Contributing
Contributions are welcome! Please fork the repository and submit a pull request with your changes.
```
