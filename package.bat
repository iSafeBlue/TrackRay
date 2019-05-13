@echo off
echo 正在删除资源文件
del /f /s /q .\resources\*.*

rd /s /q .\resources\

mkdir resources

del /f /s /q .\trackray.jar

echo 正在重新编译

mvn clean package

pause & exit 