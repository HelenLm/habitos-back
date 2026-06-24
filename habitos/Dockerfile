# Etapa 1: Construcción usando Java 25 (Eclipse Temurin)
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
# Damos permisos de ejecución al wrapper por si acaso
RUN chmod +x mvnw
# Forzamos la codificación UTF-8 para evitar errores con acentos o caracteres especiales
RUN ./mvnw clean package -DskipTests -Dfile.encoding=UTF-8

# Etapa 2: Ejecución usando un entorno ligero de Java 25
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]