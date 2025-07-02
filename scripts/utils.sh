#!/bin/bash

# config
PROFILES=("dev" "prod" "test")
VERSION_FILE="VERSION.txt"
MAIN_COMPOSE_FILE="docker-compose.yml"
APP_NAME="wordsapp"
APP_BUILD_EXT="jar"
LOG_FILE="$APP_NAME.build.log"

# Docker
USER="97f5d"
BACKEND_IMAGE_NAME="wordsapp-backend"
BACKEND_SERVICE_NAME="backend"

# dirs
COMPOSE_PROFILE_DIR="docker"
SCRIPTS_DIR="scripts"
CONFIG_DIR="config"
GRADLE_JAR_BUILD_OUTPUT="build/libs"
LOG_DIR="logs"

# check whether the file exists and if not - fails
function check_exists() {
    if [ ! -f "$1" ]; then
        echo "❌ File not found: '$1'" >&2
        exit 1
    fi
}

# check whether the dir exists
function check_exists_dir() {
    if [ ! -d "$1" ]; then
        echo "❌ Directory not found here: $1" >&2
        exit 1
    fi
}

# check whether the first argument is empty, 
# second argument is the property name
function check_empty() {
    if [ -z "$1" ]; then
        echo "❌ Value [$2] is empty: '$1'" >&2
        exit 1
    fi
}

# check whether the .jar build file exist
function check_jar_build() {
    check_exists "$GRADLE_JAR_BUILD_OUTPUT/$APP_NAME-$VERSION.$APP_BUILD_EXT"
}

# check passed profile
function check_profile() {
    if [[ ! " ${PROFILES[*]} " =~ [[:space:]]$1[[:space:]] ]]; then
        echo "❌ The profile: '$1' is not recognized!" >&2
        exit 1
    fi
    echo "✅ Recognized profile: '$1'"
}

# check whether the .env file exist
function check_env() {
    check_exists "$1"

    echo "✅ Found required env file: $1"
}

# safe export (fail if empty) the VERSION env var 
function safe_export_version() {
    check_version

    export VERSION=$(head -n 1 $VERSION_FILE)
    echo "✅ Exported version: $VERSION"
}

# checks all compose files, such as the main and the below one:
# args: 1- profile-specific compose file
function check_docker_compose_files() {
    check_exists "$MAIN_COMPOSE_FILE"
    check_exists "$1"

    echo "✅ Found required docker compose files: $MAIN_COMPOSE_FILE, $1"
}

function ask_version() {
    read -p "❔ Enter the desired build version (default: $VERSION): " version 
    if [ -z "$version" ]; then
        printf "\tThe version is empty, leaving default: $VERSION\n"
    else
        VERSION=$version
    fi
}

function check_version() {
    if [ ! -s "$VERSION_FILE" ]; then
        echo "❌ The version file '$VERSION_FILE' either doesn't exist or empty" >&2
        exit 1
    fi
}

# args:  1- profile compose file
function compose_build() {
    echo "🐳 Building service '$BACKEND_SERVICE_NAME'"

    mkdir -p "$LOG_DIR"

    docker compose \
        -f "$MAIN_COMPOSE_FILE" -f "$1"  \
        build --no-cache "$BACKEND_SERVICE_NAME" \
        >> "$LOG_DIR/$LOG_FILE" 2>&1
}

function make_jar_file() {
    echo "📦 Building the jar file of app"

    check_empty "$VERSION" "version"

    ./gradlew clean bootJar -Pversion="$VERSION" 1> /dev/null

    echo "✅ Jar file was built"
}

# copies the jar file from the gradle build path to the root 
function copy_jar_file() {
    local jarFile="$GRADLE_JAR_BUILD_OUTPUT/$APP_NAME-$VERSION.$APP_BUILD_EXT"

    check_exists "$jarFile"

    cp "$jarFile" "."

    echo "✅ Copied temp '$jarFile' file to '.'"
}

# asks for persmission to remove dangling ("<none>") images
function ask_to_kill_danglings() {
    mkdir -p "$LOG_DIR"

    read -p "❓ Kill all dangling ('<none>') images? (y/n): " shouldKill 

    [ "$shouldKill" != "y" ] && return
    
    docker rmi $(docker images -f "dangling=true" -q) -f >> "$LOG_DIR/$LOG_FILE" 2>&1 # TODO || true
}

# runs all services, first arg is the profile compose file
function compose_up() {
    mkdir -p "$LOG_DIR"

    docker compose \
        -f "$MAIN_COMPOSE_FILE" -f "$1" \
        up -d --remove-orphans \
        >> "$LOG_DIR/$LOG_FILE" 2>&1 

    echo "✅ Containers started"
}

# starts a particular service
# args: 1 - the desired compose file, might empty, 2 - service name
function compose_up_one() {
    mkdir -p "$LOG_DIR"

    docker compose \
        -f "$MAIN_COMPOSE_FILE" -f "$1" \
        up -d --remove-orphans $2 \
        >> "$LOG_DIR/$LOG_FILE" 2>&1 

    echo "✅ The service '$2' is started"
}

# copies a files,
# args: 1 - the is the file (full path + name), 2 - there to copy (dir)
function copy_file() {
    local file="$1"
    local dest="$2"

    check_exists "$file"
    check_exists_dir "$dest"

    printf "\tFile found: $file, will be copied to $dest\n"

    cp "$file" "$dest"
}