FROM openjdk:17-jdk-slim

LABEL maintainer="Analytics Dashboard Team"
LABEL description="Spring Boot Analytics Dashboard Backend"

WORKDIR /app

# Copy Maven files
COPY pom.xml .
COPY src ./src

# Install Maven
RUN apt-get update && apt-get install -y maven && rm -rf /var/lib/apt/lists/*

# Build application
RUN mvn clean package -DskipTests

# Expose port
EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
    CMD curl -f http://localhost:8080/api/track/ping || exit 1

# Run application
ENTRYPOINT ["java", "-jar", "/app/target/analytics-dashboard-1.0.0.jar"]
