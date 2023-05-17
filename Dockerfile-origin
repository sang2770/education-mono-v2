FROM adoptopenjdk/openjdk11-openj9:ubi-minimal-jre

# Set the current working directory inside the image
WORKDIR /app

COPY ./target/*.jar /app/education-0.0.1.jar
ADD ./src/main/resources /app/config
ENV ENV=prod
ENV POSTGRES_URI=jdbc:postgresql://34.171.66.5:5432/

ENV POSTGRES_USER=admin
ENV POSTGRES_PASSWORD=admin
ENV JWK_URI=http://localhost:8080/api/certificate/.well-known/jwks.json
EXPOSE 80
// ENTRYPOINT
ENTRYPOINT ["java","-jar","education-0.0.1.jar","--spring.profiles.active=${ENV:dev}"]
