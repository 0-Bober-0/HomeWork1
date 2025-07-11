FROM eclipse-temurin:24-jdk AS build

RUN apt-get update && apt-get install -y maven

WORKDIR /app
COPY . .

RUN mvn clean install

EXPOSE 8081

CMD ["java", "-jar", "target/WeatherConsumer-0.0.1-SNAPSHOT.jar"]