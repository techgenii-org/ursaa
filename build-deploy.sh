#!/usr/bin/env bash
./mvnw clean package -DskipTests
scp -i ursaa-server.pem target/ursaa-1.0.0.jar ubuntu@54.196.196.124:~/
#ssh -i ursaa-server.pem ubuntu@54.196.196.124 "kill -9 $(lsof -t -i:8080)"
#ssh -i ursaa-server.pem ubuntu@100.25.196.71 "nohup java -jar ~/ursaa-1.0.0.jar > app.log 2>&1 &"
#ssh -i ursaa-server.pem ubuntu@100.25.196.71 "nohup java -jar -Dspring.profiles.active=server ~/ursaa-1.0.0.jar > app.log &"


