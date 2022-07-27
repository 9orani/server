FROM gradle:7.3.2-jdk17

WORKDIR /home/gorani

COPY dependencies .
COPY spring-boot-loader .
COPY snapshot-dependencies .
COPY application .

RUN ln -sf /usr/share/zoneinfo/Asia/Seoul /etc/localtime

ENTRYPOINT ["java", "-Duser.timezone=Asia/Seoul", "org.springframework.boot.loader.JarLauncher"]
