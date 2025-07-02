#!/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

# config
DB_COMPOSE_PROD="$COMPOSE_PROFILE_DIR/docker-compose.prod.yml"
DB_COMPOSE_TEST="$COMPOSE_PROFILE_DIR/docker-compose.test.yml"

PROD_ENV_FILE="$CONFIG_DIR/.env.prod"
TEST_ENV_FILE="$CONFIG_DIR/.env.test"

# greet
echo "🚨 🚨 🚨 Production build 🚨 🚨 🚨"
echo "🚧 Going to build the app, test is and put it to the tagged with version Docker container!"

# leave time to think is it worth it
function build_duration_message() {
    for (( i=5 ; i > 0; i-- )) do
        echo -ne "\r🚨 Build starts in: $i "
        sleep 1
    done
    echo -e "\r🚀 Build starts now! "
}
build_duration_message

# create log dir if not done before
mkdir -p "$LOG_DIR"

# tell about new build
printf "\n\n\n\n[$(date)] New run of 'docker-build.sh'\n\n" >> "$LOG_DIR/$LOG_FILE"
 
# checks
check_env "$PROD_ENV_FILE"
check_env "$TEST_ENV_FILE"

safe_export_version

check_docker_compose_files "$DB_COMPOSE_PROD"
check_docker_compose_files "$DB_COMPOSE_TEST"

test_app() (
    source "$TEST_ENV_FILE"

    echo "👉 Starting test DB container"
    compose_up_one "$DB_COMPOSE_TEST" "db_test"
    
    # override DB connection URL for the testing
    export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:${POSTGRES_HOST_PORT}/wordsapp"

    echo "👉 Testing app"
    ./gradlew clean test 1>> "$LOG_DIR/$LOG_FILE"

    echo "🔥 App has been successfully tested!"

    echo "👉 Stopping test containers"
    bash scripts/docker-stop.sh test >> "$LOG_DIR/$LOG_FILE" >&2
)
test_app

# jar file party 
make_jar_file
copy_jar_file

source "$PROD_ENV_FILE"

echo "👉 Building an image"
compose_build "$DB_COMPOSE_PROD"

# remove temp jar file
echo "👉 Removing temp jar file"
rm "$APP_NAME-$VERSION.$APP_BUILD_EXT"

# remove "<none>"-named images
ask_to_kill_danglings

echo "🎉 The production-ready image ($USER/$BACKEND_IMAGE_NAME:$VERSION) is ready!"