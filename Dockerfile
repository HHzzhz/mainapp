FROM maven:3.6-jdk-8

RUN mkdir -p /app
COPY . /app
WORKDIR /app

ENV JAVA_HOME=
EXPOSE 8080

CMD ["mvn", "spring-boot:run"]
