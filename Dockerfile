FROM springboot:latest


RUN git clone https://github.com/Asha-go/mainapp.git
RUN cd mainapp && mvn package -P${ENV}

EXPOSE 8080 8090 8443

RUN java -jar mainapp.jar
