# Etapa 1: Construcción usando Java 25 (Eclipse Temurin)
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY . .
# Damos permisos de ejecución al wrapper por si acaso
RUN chmod +x mvnw
# Usamos el wrapper de tu proyecto para compilar
RUN ./mvnw clean package -DskipTests

# Etapa 2: Ejecución usando un entorno ligero de Java 25
FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]