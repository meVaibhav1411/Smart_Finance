FROM eclipse-temurin:17-jdk

# Install Maven
RUN apt-get update && apt-get install -y maven

WORKDIR /app

# Copy full source code
COPY . .

# Build the app inside Docker
RUN mvn clean package -DskipTests

# Run the JAR (auto-detected)
CMD ["java", "-jar", "target/smart-finance-0.0.1-SNAPSHOT.jar"]
