#!/bin/sh

port=`lsof -i :8081 -t`

kill -9 $port

cp src/main/resources/devapplication.yml src/main/resources/application.yml
nohup ./gradlew bootRun > gradlew.log 2>&1 &
