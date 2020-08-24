FROM maven:3.6-jdk-8

RUN mkdir -p /app
COPY . /app
WORKDIR /app

EXPOSE 8080

CMD ["mvn", "spring-boot:run", "-Dspring-boot.run.arguments='--spring.config.location=/application-config/application.properties'"]
