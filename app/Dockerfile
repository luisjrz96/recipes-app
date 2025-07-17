FROM gradle:8.14.3-jdk21-alpine AS build
WORKDIR /app_jar
COPY --chown=gradle:gradle . /app_jar
RUN gradle clean build --no-daemon

FROM amazoncorretto:21-alpine3.21-jdk
WORKDIR /app_jar
COPY --from=build /app_jar/build/libs/recipes-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8087
ENTRYPOINT ["java", "-jar", "/app/app.jar"]