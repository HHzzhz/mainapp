FROM maven:3.6-openjdk-8

RUN mkdir -p /app
COPY . /app
WORKDIR /app

EXPOSE 8080
ENV JAVA_HOME /usr/local/openjdk-8


CMD ["mvn", "spring-boot:run"]
