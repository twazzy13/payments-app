FROM amazoncorretto:25-jdk AS build

# Set the working directory inside the container
WORKDIR /payments-app
ARG APP_VERSION=0.0.1-SNAPSHOT
LABEL application.version="${APP_VERSION}"
# Copy the application JAR file from the 'target' (Maven) or 'build/libs' (Gradle) directory
# Replace 'your-application-0.0.1-SNAPSHOT.jar' with your actual JAR file name
COPY target/payments-app-${APP_VERSION}.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]