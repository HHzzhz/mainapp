FROM maven:3.6-jdk-8

RUN mkdir -p /app
COPY . /app
WORKDIR /app

RUN echo $JAVA_HOME

ENV JAVA_HOME /usr/local/openjdk-8

RUN echo $JAVA_HOME

EXPOSE 8080

CMD ["mvn", "spring-boot:run"]
