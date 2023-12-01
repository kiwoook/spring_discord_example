FROM openjdk:17-alpine AS builder

COPY . /tmp
WORKDIR /tmp

RUN sed -i 's/\r$//' ./gradlew
RUN ./gradlew build

FROM openjdk:17-alpine
COPY --from=builder /tmp/build/libs/*.jar ./

CMD ["java","-jar", "spring_discord-example-0.0.1-SNAPSHOT.jar"]

