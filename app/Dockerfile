# Etapa 1: Compilar la aplicación
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# Copia el pom y el código fuente (asumiendo que el Dockerfile está al mismo nivel que el pom.xml)
COPY pom.xml .
COPY src ./src

# Construir proyecto saltando pruebas
RUN mvn -B -DskipTests=true clean package

# Etapa 2: Imagen de ejecución ligera
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copiar el archivo jar compilado desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Ejecutar la aplicación mapeando el puerto dinámico de Render
CMD ["sh", "-c", "java -Dserver.port=${PORT:-4002} -jar /app/app.jar"]