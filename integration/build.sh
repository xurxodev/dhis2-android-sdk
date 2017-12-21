#!/bin/bash
set -xe

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/

# This will: compile the project, run lint, run tests under JVM, package apk, check the code quality and run tests on the device/emulator.
"$PROJECT_DIR"/gradlew --no-daemon clean build
"$PROJECT_DIR"/gradlew --no-daemon build
if [ "$TRAVIS_BRANCH" == "development" ] || [ "$TRAVIS_BRANCH" == "master" ];
then "$PROJECT_DIR"/gradlew --no-daemon connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=medium; fi
if [ "$TRAVIS_BRANCH" == "master" ];
then "$PROJECT_DIR"/gradlew --no-daemon connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=large; fi