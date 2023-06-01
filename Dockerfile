#
# Build stage
#
FROM maven:3.8.1-openjdk-11 AS build
WORKDIR /app
COPY . .
RUN mvn dependency:purge-local-repository
RUN mvn -s settings.xml clean package -DskipTests


# Build docker
FROM openjdk:11-jre-slim
WORKDIR /app
COPY --from=build /app/target/*.jar education-0.0.1.jar
ENV ENV=prod
ENV POSTGRES_URI jdbc:postgresql://dpg-chf4pcm4dad1jqfnjq60-a.oregon-postgres.render.com:5432/education_c1ny
ENV POSTGRES_USER admin
ENV POSTGRES_PASSWORD MZiokZdlp9LOV47joF91zUfEaVosfXMU
ENV JWK_URI https://education-mono-service.onrender.com/api/certificate/.well-known/jwks.json
# ADD ./src/main/resources /app/config
EXPOSE 80 9999

ENTRYPOINT ["java","-jar","education-0.0.1.jar", "--spring.profiles.active=${ENV:prod}"]