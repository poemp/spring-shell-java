@echo off
echo Install Maven Jar

cd ..
call mvn clean install -Dmaven.test.skip=true
cd  ./target
call java -jar spring-shell-java-0.0.1-SNAPSHOT.jar --spring.profiles.active=dev