@echo off
echo Install Maven Jar

cd ..
call mvn clean install -Dmaven.test.skip=true

cd ./bin

 pause