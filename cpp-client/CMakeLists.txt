cmake_minimum_required(VERSION 3.22)
project(cpp_client LANGUAGES CXX)

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} /utf-8")

# ----------------- vcpkg toolchain -----------------
if(NOT DEFINED CMAKE_TOOLCHAIN_FILE AND DEFINED ENV{VCPKG_ROOT})
  set(CMAKE_TOOLCHAIN_FILE "$ENV{VCPKG_ROOT}/scripts/buildsystems/vcpkg.cmake" CACHE STRING "")
endif()

# ----------------- dependencies --------------------
find_package(Protobuf CONFIG REQUIRED)
find_package(gRPC CONFIG REQUIRED)          # gRPC::grpc++ & gRPC::grpc_cpp_plugin

# ----------------- proto codegen -------------------
set(GENERATED_DIR ${CMAKE_CURRENT_BINARY_DIR}/generated)
file(MAKE_DIRECTORY ${GENERATED_DIR})

# ---------- ❶ object_repository proto (保持原样) ----------
set(OBJ_PROTO ${CMAKE_CURRENT_SOURCE_DIR}/object_repository.proto)
protobuf_generate_cpp(OBJ_SRC OBJ_HDR ${OBJ_PROTO} PROTOC_OUT_DIR ${GENERATED_DIR})

get_target_property(GRPC_CPP_PLUGIN_EXECUTABLE gRPC::grpc_cpp_plugin LOCATION)
add_custom_command(
  OUTPUT  ${GENERATED_DIR}/object_repository.grpc.pb.cc
          ${GENERATED_DIR}/object_repository.grpc.pb.h
  COMMAND protobuf::protoc
          ARGS --grpc_out=${GENERATED_DIR}
               --plugin=protoc-gen-grpc=${GRPC_CPP_PLUGIN_EXECUTABLE}
               -I ${CMAKE_CURRENT_SOURCE_DIR}
               ${OBJ_PROTO}
  DEPENDS ${OBJ_PROTO}
  COMMENT "Generating gRPC sources (ObjectRepository)"
)

# ---------- ❷ heartbeat_service proto (新增) ----------
set(HB_PROTO ${CMAKE_CURRENT_SOURCE_DIR}/heartbeat_service.proto)
add_custom_command(
  OUTPUT  ${GENERATED_DIR}/heartbeat_service.pb.cc
          ${GENERATED_DIR}/heartbeat_service.pb.h
          ${GENERATED_DIR}/heartbeat_service.grpc.pb.cc
          ${GENERATED_DIR}/heartbeat_service.grpc.pb.h
  COMMAND protobuf::protoc
          ARGS -I ${CMAKE_CURRENT_SOURCE_DIR}
               --cpp_out=${GENERATED_DIR}
               --grpc_out=${GENERATED_DIR}
               --plugin=protoc-gen-grpc=${GRPC_CPP_PLUGIN_EXECUTABLE}
               ${HB_PROTO}
  DEPENDS ${HB_PROTO}
  COMMENT "Generating gRPC sources (HeartbeatService)"
)

# ---------- ❸ 集合所有生成源 ----------
set(GEN_SRCS
    ${OBJ_SRC}  ${OBJ_HDR}
    ${GENERATED_DIR}/object_repository.grpc.pb.cc
    ${GENERATED_DIR}/heartbeat_service.pb.cc
    ${GENERATED_DIR}/heartbeat_service.grpc.pb.cc
)

# ----------------- target --------------------------
add_executable(client
    main.cpp
    ${GEN_SRCS}
)

target_include_directories(client PRIVATE ${GENERATED_DIR})
target_link_libraries(client PRIVATE gRPC::grpc++ protobuf::libprotobuf)

# ---------- ❹ 如需 C++20 可改这里 ----------
target_compile_features(client PRIVATE cxx_std_17)
target_compile_definitions(client PRIVATE WIN32_LEAN_AND_MEAN NOMINMAX NOGDI)
