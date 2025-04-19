FROM openjdk:21
COPY ./build/libs/notification-handler-0.0.1-SNAPSHOT.jar notification-handler.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8446
ENTRYPOINT ["java","-jar","/notification-handler.jar"]