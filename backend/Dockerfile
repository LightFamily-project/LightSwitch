# FROM azul/zulu-openjdk:17
# LABEL authors="eunhwa"


# ARG JAR_FILE=build/libs/*.jar
# COPY ${JAR_FILE} app.jar


# ENTRYPOINT ["java" , "-jar", "/app.jar"]

# RUN \
#     apt-get update && \
#     apt-get install -y vim \

FROM ghcr.io/graalvm/jdk-community:17 as builder
WORKDIR /app
COPY . .
RUN gu install native-image
RUN ./gradlew build

FROM gcr.io/distroless/base
COPY --from=builder /app/build/native/nativeCompile/lightswitch .
EXPOSE 8080

CMD ["./lightswitch"]