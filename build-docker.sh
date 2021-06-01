#!/bin/bash

set -e

docker build \
  -f src/main/docker/initContainer.Dockerfile \
  -t kilmajster/keycloak-username-password-attribute-authenticator:0.0.1 \
  -t kilmajster/keycloak-username-password-attribute-authenticator:latest \
  .