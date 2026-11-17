@echo off
g++ main.cpp -o healthcare.exe
if %errorlevel% neq 0 (
    echo Compilation failed.
    pause
    exit /b
)
echo Compilation successful.
echo.
healthcare.exe
pause
