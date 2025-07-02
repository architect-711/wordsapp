#!/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

# config
profile="$1"
envFile="config/.env.$profile"
dockerComposeFile="docker-compose.yml"
profileDockerComposeFile="docker/docker-compose.$profile.yml"

# check configs
check_profile "$profile"
check_env "$envFile" && source "$envFile"
check_docker_compose_files "$dockerComposeFile" "$profileDockerComposeFile"

# start database
echo "✅ Starting database service: db_$profile"

docker compose stop

# pass both files because otherwise it creates a standalone, non-compose
# related container that reserves required ports when running `docker-run.sh`
docker compose \
    -f "$dockerComposeFile" -f "$profileDockerComposeFile" \
    up -d --remove-orphans \
    "db_$profile" \