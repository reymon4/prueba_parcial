# docker build -t app_books .
#docker build -t docker.io/reymon4/app_books .
#docker push reymon4/app_books


FROM eclipse-temurin:21.0.5_11-jre-alpine
RUN mkdir /app
WORKDIR /app
COPY build/quarkus-app/app ./app
COPY build/quarkus-app/lib ./lib
COPY build/quarkus-app/quarkus ./quarkus
COPY build/quarkus-app/quarkus-run.jar app.jar
CMD ["java", "-jar", "app.jar"]