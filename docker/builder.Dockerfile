FROM openjdk:11-oracle

RUN mkdir -p /opt
WORKDIR /opt/src
ADD . /opt/src/

VOLUME $HOME/.gradle
VOLUME /opt/src/.gradle

RUN ./gradlew