# =========================================================
# Etapa 1: Compilar la aplicación (Build Stage)
# =========================================================
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app

# 1. Copiar los archivos de configuración de Maven
COPY pom.xml .

# 2. Copiar el código fuente de la aplicación
COPY src ./src

# 3. Compilar y empaquetar el JAR (saltando los tests para acelerar el despliegue)
RUN mvn -B -DskipTests=true clean package

# =========================================================
# Etapa 2: Imagen ligera de ejecución (Runtime Stage)
# =========================================================
FROM eclipse-temurin:21-jre
WORKDIR /app

# 4. Copiar el archivo JAR generado desde la etapa de compilación
COPY --from=build /app/target/*.jar app.jar

# 5. Comando de inicio limpio. 
# Delegamos la configuración del puerto a las variables de entorno de Render
ENTRYPOINT ["java", "-jar", "/app/app.jar"]