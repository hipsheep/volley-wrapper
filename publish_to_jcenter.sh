#!/bin/bash

# Clean and build project
./gradlew clean
./gradlew build

# Create Maven files
./gradlew install

# Publish to Bintray (JCenter)
./gradlew bintrayUpload