FROM openjdk:21
COPY ./build/libs/social-profile-0.0.1-SNAPSHOT.jar social-profile.jar
ENV SPRING_PROFILES_ACTIVE=docker
EXPOSE 8445
ENTRYPOINT ["java","-jar","/social-profile.jar"]