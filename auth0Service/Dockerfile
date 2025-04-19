FROM openjdk:21
COPY ./build/libs/auth0Service-0.0.1-SNAPSHOT.jar auth0Service.jar
EXPOSE 8443
ENTRYPOINT ["java","-jar","/auth0Service.jar"]