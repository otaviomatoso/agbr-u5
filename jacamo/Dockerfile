# Dockerfile to run AG-BR MAS application

# to build:
#    ./gradlew clean compileJava  copyToLib
#    docker build  -t agbr/u5:0.1 .
# to run
#    docker run -it agbr/u5
#
# to send to dockerhub user jomifred as version 0.1
#    docker login -u agbr
#    docker push agbr/u5:0.1

FROM alpine

ENV JAVA_HOME /usr/lib/jvm/java-1.8-openjdk
ENV PATH $PATH:$JAVA_HOME/bin

RUN apk add --update --no-cache openjdk8-jre bash
#graphviz gradle git fontconfig ttf-dejavu

COPY . /app/

#RUN cd /app \
#    && mkdir lib \
#    && ./gradlew -i jar

EXPOSE 3271
EXPOSE 3272
EXPOSE 3273
EXPOSE 8080

WORKDIR /app

#CMD ["./gradlew --offline run"]
#CMD ["bash"]
CMD ["java", "-cp", "build/classes/java/main:build/libs/*","jacamo.infra.JaCaMoLauncher", "src/jcm/main.jcm"]
