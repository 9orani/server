#!/bin/bash

if [ "$#" -lt 1 ]; then
	exit 1
fi

args=("$@")

aws --endpoint-url=https://kr.object.ncloudstorage.com s3 sync s3://gorani/server/${args[0]}.tar.gz .

if [ -d application ]
then
	rm -r dependencies snapshot-dependencies spring-boot-loader application
fi

tar -zxvf ${args[0]}.tar.gz
rm ${args[0]}.tar.gz

docker build -t gorani-spring:prod -f spring.dockerfile .

docker-compose up -d
