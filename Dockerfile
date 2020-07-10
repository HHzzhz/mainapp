FROM centos:7

RUN git clone 
RUN mvn package
RUN java mainapp.jar
