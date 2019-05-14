@echo off

del /f /s /q .\trackray.jar

echo 正在重新编译

mvn clean package

pause & exit 