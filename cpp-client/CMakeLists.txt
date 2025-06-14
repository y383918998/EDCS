cmake_minimum_required(VERSION 3.22)
project(cpp_client LANGUAGES CXX)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /utf-8")

# ----------------- vcpkg toolchain -----------------
if(NOT DEFINED CMAKE_TOOLCHAIN_FILE AND DEFINED ENV{VCPKG_ROOT})
  set(CMAKE_TOOLCHAIN_FILE "$ENV{VCPKG_ROOT}/scripts/buildsystems/vcpkg.cmake" CACHE STRING "")
endif()

# ----------------- dependencies --------------------
find_package(Protobuf CONFIG REQUIRED)
find_package(gRPC CONFIG REQUIRED)          # 提供 gRPC::grpc++ & gRPC::grpc_cpp_plugin

# ----------------- proto codegen -------------------
set(PROTO_FILE ${CMAKE_CURRENT_SOURCE_DIR}/object_repository.proto)
set(GENERATED_DIR ${CMAKE_CURRENT_BINARY_DIR}/generated)

file(MAKE_DIRECTORY ${GENERATED_DIR})

# 1) 普通 .pb.cc/.pb.h
protobuf_generate_cpp(
  PROTO_SRC  PROTO_HDR
  ${PROTO_FILE}
  PROTOC_OUT_DIR ${GENERATED_DIR}
)

# 2) gRPC Stub .grpc.pb.cc/.h —— 用自定义命令
get_target_property(GRPC_CPP_PLUGIN_EXECUTABLE gRPC::grpc_cpp_plugin LOCATION)

add_custom_command(
  OUTPUT  ${GENERATED_DIR}/object_repository.grpc.pb.cc
          ${GENERATED_DIR}/object_repository.grpc.pb.h
  COMMAND protobuf::protoc
          ARGS --grpc_out=${GENERATED_DIR}
               --plugin=protoc-gen-grpc=${GRPC_CPP_PLUGIN_EXECUTABLE}
               -I ${CMAKE_CURRENT_SOURCE_DIR}
               ${PROTO_FILE}
  DEPENDS ${PROTO_FILE}
  COMMENT "Generating gRPC sources"
)

set(GRPC_SRC  ${GENERATED_DIR}/object_repository.grpc.pb.cc)
set(GRPC_HDR  ${GENERATED_DIR}/object_repository.grpc.pb.h)

# ----------------- target --------------------------
add_executable(client
    main.cpp
    ${PROTO_SRC} ${PROTO_HDR}
    ${GRPC_SRC}  ${GRPC_HDR}
)

target_include_directories(client PRIVATE ${GENERATED_DIR})
target_link_libraries(client PRIVATE gRPC::grpc++ protobuf::libprotobuf)
target_compile_features(client PRIVATE cxx_std_17)
target_compile_definitions(client PRIVATE WIN32_LEAN_AND_MEAN NOMINMAX NOGDI)
