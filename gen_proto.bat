@echo off
setlocal

:: 设置 proto 源文件目录
set PROTO_DIR=proto

:: 设置输出目录（Python 源码输出路径）
set OUT_DIR=.

:: 编译 proto 文件
echo 🔄 Re-generating Python gRPC files from .proto...
python -m grpc_tools.protoc -I%PROTO_DIR% --python_out=%OUT_DIR% --grpc_python_out=%OUT_DIR% %PROTO_DIR%\object_repository.proto

if %ERRORLEVEL% neq 0 (
    echo ❌ Failed to compile .proto file.
    exit /b %ERRORLEVEL%
) else (
    echo ✅ Successfully generated *_pb2.py files.
)

endlocal
pause
