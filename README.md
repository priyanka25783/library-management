# Library Management System (Spring Boot + MySQL + built-in frontend)

One project, one deployable JAR. The frontend lives in `src/main/resources/static/index.html`
and is served by the same Spring Boot app that exposes the REST APIs.

## Run locally (Command Prompt)
1. `check-install.bat`                 (verifies Java, Maven, MySQL, Git)
2. `run-local.bat`                     (asks for your MySQL password, creates DB `librarydb`, starts app)
3. Open http://localhost:8080
4. Admin login: `admin` / `admin123`   (change with ADMIN_USER / ADMIN_PASSWORD env vars)

## Deploy to Railway
```
git init
git add .
git commit -m "library app"
npm install -g @railway/cli
railway login
railway init
railway add --database mysql
railway up
```
In the Railway dashboard, open your app service -> Variables and add:
```
MYSQLHOST     = ${{MySQL.MYSQLHOST}}
MYSQLPORT     = ${{MySQL.MYSQLPORT}}
MYSQLDATABASE = ${{MySQL.MYSQLDATABASE}}
MYSQLUSER     = ${{MySQL.MYSQLUSER}}
MYSQLPASSWORD = ${{MySQL.MYSQLPASSWORD}}
ADMIN_USER    = (your admin username)
ADMIN_PASSWORD= (a strong password)
```
Then Settings -> Networking -> Generate Domain to get your public URL.

## REST API
Public:  GET /api/books?q=  |  GET /api/books/{id}/file
Auth:    POST /api/auth/login  |  POST /api/auth/logout
Admin (Bearer token): /api/admin/books, /api/admin/students, /api/admin/issues
