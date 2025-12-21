# Build stage
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Run stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Copy markdown files (if they are needed at runtime)
# The MarkdownParserService currently looks for them in a specific path.
# We should probably bundle them or point to a volume.
# For now, let's assume they are in /app/questions
COPY *.md /app/questions/

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=prod"]
