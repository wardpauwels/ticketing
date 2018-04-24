FROM openjdk:8
ADD target/ticketing-1.0.0-SNAPSHOT.jar ticketing-1.0.0-SNAPSHOT.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ticketing-1.0.0-SNAPSHOT.jar"]