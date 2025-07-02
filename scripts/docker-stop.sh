#!/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

# config
PROFILE="$1"
ENV_FILE="$CONFIG_DIR/.env.$PROFILE"

DB_COMPOSE_FILE="$COMPOSE_PROFILE_DIR/docker-compose.$PROFILE.yml"

# checks
check_profile "$PROFILE"
check_env "$ENV_FILE" && source "$ENV_FILE"
check_docker_compose_files "$DB_COMPOSE_FILE"

# stop
echo "👉 Stopping active containers"
docker compose \
    -f "$MAIN_COMPOSE_FILE" -f "$DB_COMPOSE_FILE" \
    stop 

echo "🎉 Stopped containers"