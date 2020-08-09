FROM maven:3.6-jdk-8

RUN mkdir -p /app
COPY . /app
WORKDIR /app

RUN chmod 777 $JAVA_HOME -R
EXPOSE 8080

CMD ["mvn", "spring-boot:run"]
