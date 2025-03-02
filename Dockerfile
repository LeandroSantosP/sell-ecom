FROM amazoncorretto:21-alpine-jdk AS builder

LABEL maintainer="skp.Tridimensional@gmail.com"

WORKDIR /builder

ARG JAR_FILE=target/*.jar

COPY ${JAR_FILE} app.jar

RUN java -Djarmode=tools -jar app.jar extract --layers --destination extract

FROM amazoncorretto:21-alpine-jdk AS staged

WORKDIR /app

COPY --from=builder /builder/extract/dependencies/ ./
COPY --from=builder /builder/extract/spring-boot-loader/ ./
COPY --from=builder /builder/extract/snapshot-dependencies/ ./
COPY --from=builder /builder/extract/application/ ./

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
