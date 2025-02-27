FROM azul/zulu-openjdk:17
LABEL authors="eunhwa"


ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# SQLite 데이터베이스 파일이 존재하는 /data 디렉터리를 생성
RUN mkdir -p /data

ENTRYPOINT ["java" , "-jar", "/app.jar"]

# RUN \
#     apt-get update && \
#     apt-get install -y vim \

# FROM ghcr.io/graalvm/jdk:ol8-java17 as builder

# WORKDIR /app
# COPY ./ /app

# RUN chmod +x gradlew
# RUN ./gradlew clean build

# ARG JAR_FILE=./build/libs/*.jar

# COPY ${JAR_FILE} app.jar

# ENTRYPOINT ["java","-jar","app.jar"]