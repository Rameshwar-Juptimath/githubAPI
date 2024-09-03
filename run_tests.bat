@echo off


echo Running Maven clean and test...
mvn clean test


IF NOT EXIST target\allure-results (
    echo Allure results directory not found. Ensure tests have been run.
    pause
    exit /b 1
)

pause
