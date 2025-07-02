#/bin/bash

# import often used functions
source "scripts/utils.sh"

# fail on any error
set -e

# config
PROFILE="$1"
DB_COMPOSE_FILE="docker/docker-compose.$PROFILE.yml"
ENV_FILE="$CONFIG_DIR/.env.$PROFILE"

# make log dir if not done before
mkdir -p "$LOG_DIR"

# notify about new docker run
printf "\n\n\n\n[$(date)] New 'docker-run.sh' call: \n\n" >> "$LOG_DIR/$LOG_FILE"

# check configs 
check_profile "$PROFILE"
check_env "$ENV_FILE" && source "$ENV_FILE"
safe_export_version
check_docker_compose_files "$DB_COMPOSE_FILE"

read -p "❔ Have you build the app before? (y/n): " builtBefore
if [ "$builtBefore" == "y" ]; then
    compose_up "$DB_COMPOSE_FILE"

    exit 0
elif [[ "$builtBefore" != "y" && "$builtBefore" != "n" ]]; then
    echo "❌ Unknown option: $builtBefore" >&2
    exit 1
fi

# stop other active containers
echo "👉 Stopping other running containers"
docker compose stop

make_jar_file
BUILD="$GRADLE_JAR_BUILD_OUTPUT/$APP_NAME-$VERSION.$APP_BUILD_EXT"

# check success
check_exists_dir "$GRADLE_JAR_BUILD_OUTPUT"
check_exists "$BUILD"
echo "✅ Found the build file: '$BUILD'"

# copy jar file from the gradle build dir to the root
# otherwise Docker can't find .jar file
copy_jar_file

# build app to the container
compose_build "$DB_COMPOSE_FILE"

# kill <none> "dangling" images
ask_to_kill_danglings

# start newly created app and DB in containers
echo "👉 Starting newly created containers"
compose_up "$DB_COMPOSE_FILE"

# remove temp build file from the root
rm "$APP_NAME-$VERSION.$APP_BUILD_EXT"
echo "✅ Removed temp build file"

echo "🎉 The app is built and runs in the Docker container!"