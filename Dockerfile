FROM maven:3.8.4-eclipse-temurin-17 AS build
ENV HOME=/tmp/app
RUN mkdir -p $HOME

COPY pom.xml $HOME
RUN mvn -f $HOME/pom.xml dependency:go-offline -B
COPY src $HOME/src
RUN mvn -f $HOME/pom.xml package

FROM eclipse-temurin:17-jdk
COPY --from=build /tmp/app/target/cronicle.jar cronicle.jar

ENV HOST=0.0.0.0 PORT=4020 ENV=$ENV
ENTRYPOINT ["java","-jar","-Dserver.port=4020","/cronicle.jar"]

EXPOSE ${PORT}
