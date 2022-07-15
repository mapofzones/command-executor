# command-executor

Status of Last Deployment:<br>
<img src="https://github.com/mapofzones/command-executor/workflows/Java%20CI/badge.svg"><br>

## Requirements

Running directly:
* java 11
* maven

Running in a container:
* Docker

## Usage

Running directly:
* `mvn package -DskipTests` or `mvn package`
* `java -jar /opt/app.jar --spring.profiles.active=prod`

Config parameters:
* `accesstoken` - your access token to DigitalOcean API
* `clusterid` - your Kubernetes cluster_id on DigitalOcean

Running in a container:
* `docker build -t command-executor:v1 .`
* `docker run --env accesstoken=<your_digitalocean_access_token> --env clusterid=<your_kubernetes_cluster_id_on_digitalocean> -it --network="host" command-executor:v1`
