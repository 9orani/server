#!/bin/bash

cp /application.properties /home/gorani/src/main/resources/application.properties

cd /home/gorani

gradle build

java -Djarmode=layertools -jar /home/gorani/build/libs/oidc-0.0.1-SNAPSHOT.jar extract
