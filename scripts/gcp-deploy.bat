@ECHO OFF

:: Build LangReader backend
mvn clean package -f ..\pom.xml

:: Upload it to PROD server
scp -i PATH_TO_PRIVATE_KEY target\langreader-backend.jar USER@IP_ADDRESS:backend/