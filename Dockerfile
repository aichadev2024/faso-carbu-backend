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
# Réglages JVM prévus pour une petite instance (512 Mo, 0,1 vCPU) :
# tas limité pour laisser de la place au reste de la JVM, GC série
# (un seul cœur), piles de threads réduites, compilation C1 seule
# pour démarrer plus vite. Modifiable via la variable JAVA_OPTS.
ENV SPRING_PROFILES_ACTIVE=prod \
    SERVER_PORT=9090 \
    UPLOAD_DIR=/app/uploads \
    JAVA_OPTS="-XX:MaxRAMPercentage=55 -XX:+UseSerialGC -Xss512k -XX:MaxMetaspaceSize=160m -XX:ReservedCodeCacheSize=48m -XX:TieredStopAtLevel=1 -XX:+ExitOnOutOfMemoryError"
EXPOSE 9090
ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
