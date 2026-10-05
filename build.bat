@echo off
where gradle >nul 2>nul || (echo Gradle not found. Run: winget install Gradle.Gradle & exit /b 1)
gradle build
if errorlevel 1 exit /b 1
echo.
echo Done. Your mod is in build\libs\ootcraft-0.1.0.jar
