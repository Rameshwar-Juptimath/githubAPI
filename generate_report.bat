@echo off

echo Generating Allure report...
allure serve target\allure-results

echo Allure report should now be open. Press any key to close this window...

pause
