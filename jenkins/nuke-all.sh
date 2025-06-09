#!/bin/bash
set -e

# Загрузка переменных из .env
if [ -f .env ]; then
  export $(grep -v '^#' .env | xargs)
fi

# Проверка переменной
if [ -z "$DOCKER_REGISTRY" ]; then
  echo "DOCKER_REGISTRY не задан в .env"
  exit 1
fi

echo "Using DOCKER_REGISTRY: $DOCKER_REGISTRY"

echo "Uninstalling Helm releases..."
for ns in test prod; do
  helm uninstall exchange-application -n "$ns" || true
  helm uninstall blocker-application -n "$ns" || true
  helm uninstall exchange-generator-application -n "$ns" || true
  helm uninstall notifications-application -n "$ns" || true
  helm uninstall accounts-application -n "$ns" || true
  helm uninstall cash-application -n "$ns" || true
  helm uninstall transfer-application -n "$ns" || true
  helm uninstall bank-ui-application -n "$ns" || true
  helm uninstall db -n "$ns" || true
  helm uninstall keycloak -n "$ns" || true
  helm uninstall kafka -n "$ns" || true
done

echo "Deleting secrets..."
for ns in test prod; do
  kubectl delete secret yabank-db -n "$ns" --ignore-not-found
  kubectl delete secret yabank-keycloak -n "$ns" --ignore-not-found
  kubectl delete secret yabank-postgresql -n "$ns" --ignore-not-found
  kubectl delete secret yabank-kafka -n "$ns" --ignore-not-found
done

echo "Deleting namespaces..."
kubectl delete ns test --ignore-not-found
kubectl delete ns prod --ignore-not-found

echo "Shutting down Jenkins..."
docker compose down -v || true
docker stop jenkins && docker rm jenkins || true
docker volume rm jenkins_home || true

echo "Removing images..."
docker image rm ${DOCKER_REGISTRY}/exchange-application:1 || true
docker image rm ${DOCKER_REGISTRY}/blocker-application:1 || true
docker image rm ${DOCKER_REGISTRY}/exchange-generator-application:1 || true
docker image rm ${DOCKER_REGISTRY}/notifications-application:1 || true
docker image rm ${DOCKER_REGISTRY}/accounts-application:1 || true
docker image rm ${DOCKER_REGISTRY}/cash-application:1 || true
docker image rm ${DOCKER_REGISTRY}/transfer-application:1 || true
docker image rm jenkins/jenkins:lts-jdk21 || true

echo "Pruning system..."
docker system prune -af --volumes

echo "Done! All clean."
