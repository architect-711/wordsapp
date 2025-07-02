#!/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

# config
PROFILE="$1"
ENV_FILE="$CONFIG_DIR/.env.$PROFILE"
DB_COMPOSE_PROFILE="$COMPOSE_PROFILE_DIR/docker-compose.$PROFILE.yml"

# check configs
check_profile "$PROFILE"
check_env "$ENV_FILE" && source "$ENV_FILE"
check_docker_compose_files "$DB_COMPOSE_PROFILE"

# Stop other containers
echo "👉 Stopping other active containers"
docker compose stop

# start database
echo "👉 Starting database service: 'db_$PROFILE'"

# pass both files because otherwise it creates a standalone, non-compose
# related container that reserves required ports when running `docker-run.sh`
compose_up_one "$DB_COMPOSE_PROFILE" "db_$PROFILE"