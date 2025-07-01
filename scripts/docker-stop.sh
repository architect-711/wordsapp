#!/bin/bash

set -e
source "scripts/utils.sh"


# config
profile="$1"
env_file="config/.env.$profile"

mainComposeFile="docker-compose.yml"
profileComposeFile="docker/docker-compose.$profile.yml"


# stop
check_profile "$profile"
check_env "$env_file"
source "$env_file"

check_docker_compose_files "$mainComposeFile" "$profileComposeFile"

docker compose \
    -f "$mainComposeFile" -f "$profileComposeFile" \
    stop