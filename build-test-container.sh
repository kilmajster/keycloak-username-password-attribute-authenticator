#!/bin/bash

set -e

docker build \
  -f src/main/docker/dev.Dockerfile \
  -t kilmajster/keycloak-username-password-attribute-authenticator:dev \
  .