FROM maven:3.6.1-jdk-11 as build

WORKDIR /opt

COPY *pom.xml /opt/

COPY . /opt/

RUN mvn package -DskipTests

FROM openjdk:11-jdk-slim

RUN apt-get update
RUN apt-get -y install curl wget apt-transport-https

RUN wget https://github.com/digitalocean/doctl/releases/download/v1.78.0/doctl-1.78.0-linux-amd64.tar.gz
RUN tar xf ./doctl-1.78.0-linux-amd64.tar.gz
RUN mv ./doctl /usr/local/bin
RUN rm ./doctl-1.78.0-linux-amd64.tar.gz

RUN curl -LO https://storage.googleapis.com/kubernetes-release/release/$(curl -s https://storage.googleapis.com/kubernetes-release/release/stable.txt)/bin/linux/amd64/kubectl
RUN chmod +x ./kubectl
RUN mv ./kubectl /usr/local/bin

COPY --from=build /opt/target/command-executor*.jar /opt/app.jar
COPY scripts/run.sh /opt/run.sh

RUN chmod +x opt/run.sh
CMD opt/run.sh
