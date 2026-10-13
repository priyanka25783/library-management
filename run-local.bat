@echo off
REM Type your MySQL password when asked. It is NOT stored in any file.
set /p DB_PASSWORD=Enter MySQL root password: 
set MYSQLUSER=root
call mvn spring-boot:run
