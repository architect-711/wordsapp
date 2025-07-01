#!/bin/bash

source "scripts/utils.sh"

set -e

VERSION="0.0.0-SNAPSHOT"

# build
echo "🔎 Going to build the JAR file, note no tests are done."

ask_version

buildFileFull="build/libs/wordsapp-$VERSION.jar"

make_jar_file

if [ ! -f "$buildFileFull" ]; then
    echo "❌ Build file not found."
    exit 1
fi
echo "🫵  Checkout the output file here: $buildFileFull"