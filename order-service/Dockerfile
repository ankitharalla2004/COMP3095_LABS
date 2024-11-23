#-----------------------------------------
#Build Stage
#-----------------------------------------

FROM gradle:8-jdk23 AS builder

COPY --chown=gradle:gradle . /home/gradle/src

WORKDIR /home/gradle/src

RUN gradle build -x test

#-----------------------------------------
#Package Stage
#-----------------------------------------
FROM openjdk:23-jdk

RUN mkdir /app

COPY --from=builder /home/gradle/src/build/libs/*.jar /app/order-service.jar

EXPOSE 9082

ENTRYPOINT ["java", "-jar", "/app/order-service.jar"]