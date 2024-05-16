FROM openjdk:17-alpine

COPY target/login-0.0.1-SNAPSHOT.jar /app/login-0.0.1-SNAPSHOT.jar

WORKDIR /app

CMD java ${JAVA_TOOL_OPTIONS} -jar login-0.0.1-SNAPSHOT.jar