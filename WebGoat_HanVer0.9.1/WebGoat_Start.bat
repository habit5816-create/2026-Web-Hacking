@echo off
set "BASE_DIR=%~dp0"
set "JAVA_HOME=%BASE_DIR%java"
set "CATALINA_HOME=%BASE_DIR%tomcat"
set "JAVA_OPTS=-Xms64m -Xmx128m"

echo [1/3] Killing existing Java processes...
taskkill /f /im java.exe 2>nul
taskkill /f /im javaw.exe 2>nul

echo [2/3] Stopping IIS Service...
net stop w3svc /y 2>nul

echo [3/3] Starting WebGoat Tomcat...
cd /d "%BASE_DIR%tomcat\bin"
call catalina.bat run

echo.
echo If you see this, Tomcat failed to start.
pause