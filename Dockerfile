FROM amazoncorretto:21-alpine-jdk

LABEL maintainer="skp.Tridimensional@gmail.com"

WORKDIR /app

COPY target/demo-sell-ecom-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/app.jar"]


