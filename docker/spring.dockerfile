FROM gradle:7.3.2-jdk17

WORKDIR /home/gorani

COPY ../configs/application.properties /application.properties
COPY ../configs/entrypoint.layer.sh /entrypoint.sh

RUN apt-get update && apt-get install -y locales
RUN localedef -f UTF-8 -i ko_KR ko_KR.UTF-8

ENV LANG=ko_KR.UTF-8 \
    LANGUAGE=ko_KR.UTF-8 \
    LC_ALL=ko_KR.UTF-8
