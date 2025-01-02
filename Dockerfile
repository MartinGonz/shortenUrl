# Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Set the working directory in the container
WORKDIR /app

# Copy the entire project directory contents into the container at /app
COPY . /app

# Install cqlsh and other necessary tools
#RUN apk update && apk add python3 py3-pip && pip3 install cqlsh

# Make the mvnw script executable
RUN chmod +x ./mvnw
# Package the application
RUN ./mvnw package

# Set JVM options for 1GB heap size
ENV JAVA_OPTS="-Xmx1536m -Xms1536m"

# Run the application
CMD ["sh", "-c", "java $JAVA_OPTS -jar target/shortUrl-0.0.1-SNAPSHOT.jar"]