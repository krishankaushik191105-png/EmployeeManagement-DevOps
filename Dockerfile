# ─────────────────────────────────────────────────────────────────────────────
# Dockerfile — Employee Management REST API
# ─────────────────────────────────────────────────────────────────────────────
#
# USAGE
# -----
# 1. Build the JAR first (from the project root on Windows):
#       gradlew.bat build -x test
#
# 2. Build the Docker image:
#       docker build -t employee-management-api:1.0.0 .
#
# 3. Run the container:
#       docker run -p 8080:8080 employee-management-api:1.0.0
#
# 4. Access the API:
#       http://localhost:8080/employees
# ─────────────────────────────────────────────────────────────────────────────

# Base image: Eclipse Temurin is the most widely used, production-grade
# OpenJDK distribution. The '-jre' variant is smaller than the full JDK
# because it only contains what is needed to *run* (not compile) Java.
FROM eclipse-temurin:17-jre

# A non-root user for running the application.
# Running as root inside a container is a security risk — if the process
# is ever compromised, the attacker has root access to the container.
RUN addgroup --system appgroup && adduser --system --ingroup appgroup appuser

# Working directory inside the container.
# All subsequent commands run relative to this path.
WORKDIR /app

# Copy the executable Spring Boot JAR produced by:  gradlew.bat build
# The fat JAR (without '-plain') contains the app + all dependencies.
COPY build/libs/employee-management-api-1.0.0.jar app.jar

# Transfer ownership so appuser can read the JAR.
RUN chown appuser:appgroup app.jar

# Switch to the non-root user before the application starts.
USER appuser

# Tell Docker (and humans) that the container listens on port 8080.
# This does NOT publish the port — use  -p 8080:8080  with docker run.
EXPOSE 8080

# Start the Spring Boot application.
# Using the exec form (JSON array) so the JVM is PID 1 and receives
# OS signals (SIGTERM) correctly for graceful shutdown.
ENTRYPOINT ["java", "-jar", "app.jar"]
