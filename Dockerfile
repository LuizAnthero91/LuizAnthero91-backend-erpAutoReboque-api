FROM eclipse-temurin:17-jdk AS build

WORKDIR /app

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

RUN ./mvnw dependency:go-offline -B

COPY src ./src

RUN ./mvnw clean package -DskipTests



FROM eclipse-temurin:17-jre

WORKDIR /app

RUN addgroup --system spring && \
    adduser --system --ingroup spring spring

COPY --from=build \
    /app/target/autoreboque-tora-0.0.1-SNAPSHOT.jar \
    app.jar

RUN chown spring:spring /app/app.jar

USER spring:spring

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]