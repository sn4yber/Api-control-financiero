# Etapa 1: Build (Construcción)
FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

# Establecer directorio de trabajo
WORKDIR /app

# Copiar archivos de configuración de Maven
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Descargar dependencias (esto se cachea si no cambia el pom.xml)
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación (saltar tests para build más rápido)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime (Ejecución)
FROM eclipse-temurin:21-jre-alpine

# Metadata
LABEL maintainer="snayber@example.com"
LABEL description="API REST Control Financiero Personal"

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Establecer directorio de trabajo
WORKDIR /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar ownership
RUN chown -R spring:spring /app

# Usar usuario no-root
USER spring:spring

# Puerto que expone la aplicación
EXPOSE 8080

# Variables de entorno optimizadas para arranque RÁPIDO en Render Free Tier
ENV JAVA_OPTS="-XX:+UseContainerSupport \
    -XX:MaxRAMPercentage=75.0 \
    -XX:InitialRAMPercentage=50.0 \
    -XX:+TieredCompilation \
    -XX:TieredStopAtLevel=1 \
    -Xverify:none \
    -XX:+UseSerialGC \
    -XX:+ExitOnOutOfMemoryError \
    -Djava.security.egd=file:/dev/./urandom \
    -Dspring.backgroundpreinitializer.ignore=true"
ENV SPRING_PROFILES_ACTIVE=prod

# Health check optimizado (verifica cada 15s, timeout 2s, start-period 30s)
HEALTHCHECK --interval=15s --timeout=2s --start-period=30s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Comando para ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Dserver.port=${PORT:-8080} -jar app.jar"]

