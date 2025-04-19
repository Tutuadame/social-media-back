FROM openjdk:21
COPY ./build/libs/communicator-0.0.1-SNAPSHOT.jar communicator.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8444
ENTRYPOINT ["java","-jar","/communicator.jar"]