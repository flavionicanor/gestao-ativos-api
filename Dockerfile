# ── Etapa 1: Build
FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /app

# Copia os arquivos de dependência primeiro (cache do Docker)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Baixa as dependências (camada cacheada se o pom.xml não mudar)
RUN ./mvnw dependency:go-offline -q

# Copia o código fonte e compila
COPY src src
RUN ./mvnw package -DskipTests -q

# ── Etapa 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

# Cria usuário não-root por segurança
RUN addgroup -S spring && adduser -S spring -G spring
USER spring

# Copia apenas o .jar gerado na etapa anterior
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT [ "java", "-jar", "app.jar" ]