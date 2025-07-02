#!/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

RESULT_DIR="$APP_NAME"

rm -rf "$RESULT_DIR"

# make dest folder structure
mkdir -p "$RESULT_DIR"
mkdir -p "$RESULT_DIR/$CONFIG_DIR"
mkdir -p "$RESULT_DIR/$SCRIPTS_DIR"

# need to get the version
safe_export_version

function copy_version() {
    check_exists "$VERSION_FILE"

    cp "$VERSION_FILE" "$RESULT_DIR"
}

function add_envs() {
    read -p "👉 Enter env files from '$CONFIG_DIR' folder to be added (ex: '.env.a:.env.b'): " envFilesString

    if [ -z "$envFilesString" ]; then
        printf "\tNo env files specified\n"
        return 0
    fi

    IFS=':' read -ra envFiles <<< "$envFilesString"

    echo "👉 Adding env files..."

    for item in "${envFiles[@]}"; do
        printf "\tAdding file: $item\n"

        if [ -f "$CONFIG_DIR/$item" ]; then
            cp "$CONFIG_DIR/$item" "$RESULT_DIR/$CONFIG_DIR/$item"
        else
            printf "\tFile not found: $item\n"
        fi
    done
}

function add_sh() {
    local docker_db_run="docker-db-run.sh"
    local docker_run="docker-run.sh"
    local docker_stop="docker-stop.sh"
    local utils="utils.sh"

    echo "👉 Adding Shell helping scripts: $docker_db_run, $docker_run, $docker_stop, $utils to $RESULT_DIR/$SCRIPTS_DIR..."

    copy_file "$SCRIPTS_DIR/$docker_db_run"  "$RESULT_DIR/$SCRIPTS_DIR"
    copy_file "$SCRIPTS_DIR/$docker_run"     "$RESULT_DIR/$SCRIPTS_DIR"
    copy_file "$SCRIPTS_DIR/$docker_stop"    "$RESULT_DIR/$SCRIPTS_DIR"
    copy_file "$SCRIPTS_DIR/$utils"          "$RESULT_DIR/$SCRIPTS_DIR"
}

function add_compose_files() {
    copy_file "$MAIN_COMPOSE_FILE" "$RESULT_DIR"
    echo "✅ Copied '$MAIN_COMPOSE_FILE' file"

    check_exists_dir "$COMPOSE_PROFILE_DIR"

    # if config dir is empty (no docker-compose.<PROFILE>.yml files)
    if [[ $(ls -A "$COMPOSE_PROFILE_DIR" | wc -l ) -eq 0 ]]; then
        echo "❌ Couldn't locate compose profile files here: '$COMPOSE_PROFILE_DIR'" >&2
        exit 1;
    fi

    cp -r "$COMPOSE_PROFILE_DIR" "$RESULT_DIR"
    echo "✅ All compose files are copied"

    copy_file "Dockerfile" "$RESULT_DIR" 1> /dev/null
    echo "✅ Dockerfile copied"
}

copy_version
add_envs
add_sh
add_compose_files

# adding docker image
DEFAULT_DOCKER_OF="$USER.$BACKEND_IMAGE_NAME.$VERSION.gz"
[ ! -f "$DEFAULT_DOCKER_OF" ] && exit 0

read -p "❔ Would you like to also copy the project Docker image .gz file? (y/n): " shouldCopy
[ -z "$shouldCopy" ] && exit 0

read -p "👉 Enter it's path (default: $DEFAULT_DOCKER_OF): " dockerOF
[ -n "$dockerOF" ] && DEFAULT_DOCKER_OF="$dockerOF"

# copy image
copy_file "$DEFAULT_DOCKER_OF" "$RESULT_DIR"
echo "✅ Copied the image"

# make tar archieve 
echo "📦 Applying tar..."
tar cf "$RESULT_DIR.tar" "$RESULT_DIR"

# compress
echo "📦 Applying gzip..."
gzip "$RESULT_DIR.tar"

echo "🎉 Mobile config is ready, you can easily copy it to the remote server"