# ---------- Étape 1 : build ----------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /app
# Dépendances d'abord, pour profiter du cache Docker
COPY pom.xml .
RUN mvn -q dependency:go-offline
COPY src ./src
RUN mvn -q clean package -DskipTests

# ---------- Étape 2 : exécution ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
RUN useradd --system --uid 1001 fasocarbu && mkdir -p /app/uploads && chown fasocarbu /app/uploads
COPY --from=build /app/target/fasocarbu-0.0.1-SNAPSHOT.jar app.jar
USER fasocarbu
ENV SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=9090 \
    UPLOAD_DIR=/app/uploads
EXPOSE 9090
ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75", "-jar", "app.jar"]
