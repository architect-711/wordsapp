#!/bin/bash

set -e
source "scripts/utils.sh"

# config
export VERSION="0.0.0-SNAPSHOT"

mainComposeFile="docker-compose.yml"
profileComposeFile="docker/docker-compose.prod.yml"

dockerLogDir="logs"
dockerLogFileFull="$dockerLogDir/docker.log"

echo "🚧 Going to build the app with tests and put it to the tagged Docker container!"

mkdir -p "$dockerLogDir"
printf "\n\n\n\n[$(date)] New run of 'docker-build.sh'\n\n" >> "$dockerLogFileFull"
 
check_docker_compose_files "$mainComposeFile" "$profileComposeFile"

function test_app() {
    local env_test="config/.env.test"

    check_env "$env_test"
    source "$env_test"

    echo "👉 Starting test DB container"
    compose_up_one "$mainComposeFile" "docker/docker-compose.test.yml" "db_test" "$dockerLogFileFull"

    export SPRING_DATASOURCE_URL="jdbc:postgresql://localhost:${POSTGRES_HOST_PORT}/wordsapp"

    echo "👉 Testing app"
    ./gradlew clean test 1>> "$dockerLogFileFull"

    echo "🔥 App has been successfully tested!"
}
test_app

bash scripts/docker-stop.sh test >> "$dockerLogFileFull" 2>&1 
echo "✅ Containers used for testing are stopped"

ask_version
make_jar_file 

copy_jar_file "build/libs/wordsapp-$VERSION.jar"  .
echo "👉 Building an image"
compose_build "$mainComposeFile" "$profileComposeFile" "$dockerLogFileFull"
rm "wordsapp-$VERSION.jar"

docker tag "97f5d/wordsapp-backend:latest" "97f5d/wordsapp-backend:$VERSION"

ask_to_prune "$dockerLogFileFull"

echo "🎉 The production-ready image (97f5d/wordsapp-backend:$VERSION) is ready!"

# build app