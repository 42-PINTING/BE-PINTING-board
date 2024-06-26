FROM openjdk:17-jdk

ARG JAR_FILE=build/libs/*.jar
ARG BOARD_PORT

COPY ${JAR_FILE} app.jar

EXPOSE ${BOARD_PORT}

ENTRYPOINT ["java","-jar","/app.jar"]