@echo off
echo ===== Checking required installations =====
echo.
echo [1/5] Java (need 17 or higher)
java -version 2>&1 || echo   NOT FOUND - install JDK 17+ from https://adoptium.net
echo.
echo [2/5] Maven
call mvn -v 2>&1 || echo   NOT FOUND - install from https://maven.apache.org/download.cgi and add bin to PATH
echo.
echo [3/5] MySQL
mysql --version 2>&1 || echo   NOT FOUND - install MySQL Server 8 from https://dev.mysql.com/downloads/
echo.
echo [4/5] Git
git --version 2>&1 || echo   NOT FOUND - install from https://git-scm.com
echo.
echo [5/5] Railway CLI (optional, needs Node.js)
call railway --version 2>&1 || echo   NOT FOUND - run: npm install -g @railway/cli
echo.
echo ===== Done =====
pause
