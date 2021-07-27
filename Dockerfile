FROM openjdk:11

COPY build/libs/timestamp-ap-0.0.1-SNAPSHOT-plain.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app.jar"]