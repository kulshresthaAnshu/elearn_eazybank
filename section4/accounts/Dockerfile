#Start with a base image containing Java runtime
FROM openjdk:17-jdk-slim

#Who is the maintainer of this image
MAINTAINER anshu

#Copy the jar from target and paste it to docker image
COPY target/accounts-0.0.1-SNAPSHOT.jar accounts-0.0.1-SNAPSHOT.jar

#Execute the application
ENTRYPOINT ["java","-jar","accounts-0.0.1-SNAPSHOT.jar" ]