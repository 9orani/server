#!/bin/bash

cp /application.properties /home/gorani/src/main/resources/application.properties

cd /home/gorani

gradle build
