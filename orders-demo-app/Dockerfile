FROM maven:3.9.6-eclipse-temurin-17-alpine AS build
ENV HOME=/usr/app
RUN mkdir -p $HOME
WORKDIR $HOME
COPY . .
RUN mvn -f $HOME/pom.xml clean install package

FROM eclipse-temurin:17-jre-jammy
ARG JAR_FILE=/usr/app/target/*.jar
COPY --from=build $JAR_FILE /app/runner.jar
EXPOSE 8089
ENTRYPOINT java -jar /app/runner.jar