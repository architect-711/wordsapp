#/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

# config
PROFILE="$1"
ENV_FILE="$CONFIG_DIR/.env.$PROFILE"
DB_COMPOSE_FILE="$COMPOSE_PROFILE_DIR/docker-compose.$PROFILE.yml"

# check configs
check_profile "$PROFILE"
check_env "$ENV_FILE" && source "$ENV_FILE"
safe_export_version
check_docker_compose_files "$DB_COMPOSE_FILE"

# stop other containers
echo "🛑 Stopping other containers"
docker compose stop

# start containers
echo "✅ Starting database service: db_$PROFILE"
# pass both files because otherwise it creates a standalone, non-compose
# related container that reserves required ports when running `docker-run.sh`
docker compose \
    -f "$MAIN_COMPOSE_FILE" -f "$DB_COMPOSE_FILE" \
    up -d --remove-orphans \
    "db_$PROFILE" \

# override, because app runs outside of Docker, so must refer to DB container host port
export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:${POSTGRES_HOST_PORT}/wordsapp"
echo "👉 Overridden SPRING_DATASOURCE_URL (see docs why): $SPRING_DATASOURCE_URL"

# start app
echo "✅ Starting app"
# dev and prod run
[[ "$PROFILE" == "dev" || "$PROFILE" == "prod" ]] && ./gradlew clean bootRun
# test run
read -p "❔ Any args for the test command?: " testArgs
[ "$PROFILE" == "test" ] && ./gradlew clean test $testArgs