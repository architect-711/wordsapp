#!/bin/bash

function check_profile() {
    local profiles=("dev" "prod" "test")

    if [[ ! " ${profiles[*]} " =~ [[:space:]]$1[[:space:]] ]]; then
        echo "❌ The profile: $1, is not recognized!"
        return 1
    fi
    echo "✅ Recognized profile: $1"
}

function check_env() {
    if [[ ! -f "$1" ]]; then
        echo "❌ Required env file not found: $1!"
        return 1
    fi

    echo "✅ Found required env file: $1"
}

# args: 1 - mainComposeFile, 2 - profileComposeFile
function check_docker_compose_files() {
    if [[ ! -f "$1" || ! -f "$2" ]]; then
        echo "❌ Either '$1' or/and '$2' docker compose file not found."
        return 1
    fi

    echo "✅ Found required docker compose files: $1, $2"
}

function ask_version() {
    read -p "❔ Enter the desired build version (default: $VERSION): " version 
    if [ -z "$version" ]; then
        printf "\tThe version is empty, leaving default: $VERSION\n"
    else
        VERSION=$version
    fi
}

#           1                   2                   3
# args: mainComposeFile, profileComposeFile, dockerLogFileFull
function compose_build() {
    echo "🐳 Building service 'backend'"

    docker compose \
        -f "$1" -f "$2"  \
        build --no-cache "backend" \
        >> "$3" 2>&1
}

function make_jar_file() {
    echo "📦 Building the jar file of app"

    if [ -z "$VERSION" ]; then
        echo "❌ Couldn't find the VERSION env var."
        return 1
    fi

    ./gradlew clean bootJar -Pversion="$VERSION" 1> /dev/null

    echo "✅ Jar file is built"
}

# args: 1 - input file full path, 2 - where to put it
function copy_jar_file() {
    local fi="$1"
    local fo="$2"

    if [ ! -f "$fi" ]; then
        echo "❌ Jar file not found here: $fi"
        return 1
    fi

    cp "$fi" "$fo"

    echo "✅ Copied temp '$fi' file to '$fo'"
}

# args: 1 - log output full path
function ask_to_prune() {
    read -p "❓ Prune all other containers? (y/n): " prune

    [ "$prune" != "y" ] && return
    
    # docker compose stop >> "$dockerLogFileFull" 2>&1
    docker rmi $(docker images -f "dangling=true" -q) -f >> "$1" 2>&1 || true
}

# args: 1 - compose file 1, 2 - compose file 2, 3 - docker log output
function compose_up() {
    docker compose \
        -f "$1" -f "$2" \
        up -d --remove-orphans \
        >> "$3" 2>&1 

    echo "✅ Containers started"
}

# args: 1 and 2 - compose files, 3 service to be started, 4 - docker log output,
function compose_up_one() {
    docker compose \
        -f "$1" -f "$2" \
        up -d --remove-orphans $3 \
        >> "$4" 2>&1 

    echo "✅ The service '$3' is started"
}