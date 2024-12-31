# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the current directory contents into the container at /app
COPY . /app

# Package the application
RUN ./mvnw package

# Set JVM options for 1GB heap size
ENV JAVA_OPTS="-Xmx1g -Xms1g"

# Run the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar target/shortUrl-0.0.1-SNAPSHOT.jar"]