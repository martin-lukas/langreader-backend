@ECHO OFF

call mvn spring-boot:run -Dspring-boot.run.arguments="--langreader.app.dev.server=http://localhost:3000 --langreader.app.googleApiKey=API_KEY" -Dspring-boot.run.jvmArguments="-agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8787"