#!/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

# check version 
safe_export_version

# build jar file
echo "🔎 Going to build the JAR file, note no tests are done."
make_jar_file

# check success
check_jar_build

echo "🎉 App has been successfully built here: '$GRADLE_JAR_BUILD_OUTPUT/$APP_NAME-$VERSION.$APP_BUILD_EXT'"