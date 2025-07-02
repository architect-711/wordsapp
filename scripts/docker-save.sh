#!/bin/bash

# import functions
source "scripts/utils.sh"

# fail on any error
set -e

# print current app images
echo "👉 Current app images: "
docker images | grep "$USER/$BACKEND_IMAGE_NAME"

# verify tag
read -p "👉 Enter the tag of image '$BACKEND_IMAGE_NAME' to save: " tag

# check is blank
check_empty "$tag" "tag"

# verify image
docker images "$USER/$BACKEND_IMAGE_NAME:$tag"
read -p "❔ Is this image correct?: (y/n) " isCorrect

# check is correct
if [[ "$isCorrect" == "n" || "$isCorrect" != "y" ]]; then
    echo "❌ Couldn't determine correct image" >&2
    exit 1
fi

# output file
OF="$USER.$BACKEND_IMAGE_NAME.$tag"

# Ask for new output file 
read -p "👉 Enter the output file of '$USER/$BACKEND_IMAGE_NAME:$tag' (default: $OF): " newOf
[ -n "$newOf" ] && OF="$newOf"

# packing
echo "📦 Saving the image to '$OF'"
docker save "$USER/$BACKEND_IMAGE_NAME:$tag" > "$OF" 

# compressing
echo "📦 Compressing output file"
gzip "$OF"

# ckeck success
check_exists "$OF.gz"
echo "🎉 Saved image '$USER/$BACKEND_IMAGE_NAME:$tag' to '$OF.gz'"