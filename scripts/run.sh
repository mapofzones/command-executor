#!/bin/bash

echo "Start setting up access credentials..."
# shellcheck disable=SC2154
if [ "$accesstoken" != "" ] && [ "$clusterid" != "" ]; then
  doctl auth init --access-token "$accesstoken" > out.log

  doctl kubernetes cluster kubeconfig save "$clusterid" >> out.log
  rm out.log

  var="$(doctl account get)"
  if [[ "$var" == *"active" ]]; then
    echo "Access to DigitalOcean granted!"
  else
    echo "Error, no access to DigitalOcean available!"
    exit 1
  fi

  var="$(kubectl auth can-i delete pods --namespace prod)"
  if [[ "$var" == "yes" ]]; then
    echo "Access to Kubernetes Cluster granted!"
  else
    echo "Error, no access to Kubernetes Cluster available!"
    exit 1
  fi

else
  echo "Need to specify accesstoken & clusterid dev env!"
  sleep 60
  exit 1
fi

echo "Finished setting up access credentials..."

echo "Starting command-executor..."
java -jar /opt/app.jar --spring.profiles.active=prod ; sleep 60
