#/bin/bash

# fail on any error
set -e

# import often used functions
source "scripts/utils.sh"

# config
export VERSION="0.0.0-SNAPSHOT"
buildFile="wordsapp-$VERSION.jar"
buildFileDir="build/libs"

profiles=("dev" "test" "prod")
profile="$1"

dockerLogDir="logs"
dockerLogFileFull="$dockerLogDir/docker.log"

profileComposeFile="docker/docker-compose.$profile.yml"
mainComposeFile="docker-compose.yml"

envFile="config/.env.$profile"
service="backend"

mkdir -p "$dockerLogDir"

# We don't clear it, we append to it
printf "\n\n\n\n[$(date)] New docker-run.sh call: \n\n" >> "$dockerLogFileFull"

# check configs 
check_profile "$profile"
check_env "$envFile" && source "$envFile"
check_docker_compose_files "$mainComposeFile" "$profileComposeFile"

read -p "❔ Have you build the app before? (y/n): " builtBefore
if [ "$builtBefore" == "y" ]; then
    compose_up "$mainComposeFile" "$profileComposeFile" "$dockerLogFileFull"

    exit 0
elif [[ "$builtBefore" != "y" && "$builtBefore" != "n" ]]; then
    echo "❌ Unknown option: $builtBefore"
    exit 1
fi

ask_version

# update build name
buildFile="wordsapp-$VERSION.jar"

# run
make_jar_file

if [[ ! -d "$buildFileDir" || ! -f "$buildFileDir/$buildFile" ]]; then
    echo "❌ Couldn't find a jar file here: $buildFileDir/$buildFile"
    exit 1
fi

copy_jar_file "$buildFileDir/$buildFile" "."
compose_build "$mainComposeFile" "$profileComposeFile" "$dockerLogFileFull"

ask_to_prune "$dockerLogFileFull"
docker compose stop
compose_up "$mainComposeFile" "$profileComposeFile" "$dockerLogFileFull"

rm "$buildFile"

echo "🎉 The app is built and runs in the Docker container!"