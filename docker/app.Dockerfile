FROM tisserv/fa-builder as build-stage

ADD . /opt/src/

RUN ./gradlew bootJar

FROM openjdk:11-oracle

COPY --from=build-stage /opt/src/build/libs/forex-analyst-1.0-SNAPSHOT.jar /opt/forex-analyze-app.jar

# Run in foreground.
CMD [ "sh", "-c", "java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=prod -jar /opt/forex-analyze-app.jar" ]
