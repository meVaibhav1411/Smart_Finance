FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

# Create app directory
WORKDIR /app

# Copy the whole project (pom.xml and src)
COPY . .

# Build the application using Maven
RUN mvn clean package -DskipTests

# Run the JAR (adjust JAR name if needed)
CMD ["java", "-jar", "target/smart-finance-0.0.1-SNAPSHOT.jar"]
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]