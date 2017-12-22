#!/bin/bash
set -xe

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/

# This will: compile the project, run lint, run tests under JVM, package apk, check the code quality and run tests on the device/emulator.
if [ "$TEST_SUITE" == "units" ];
then
"$PROJECT_DIR"/gradlew clean
"$PROJECT_DIR"/gradlew build -Dscan
"$PROJECT_DIR"/gradlew test
"$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small; fi
if [ "$TEST_SUITE" == "integration" ]
then
    if [ "$TRAVIS_BRANCH" == "development" ] || [ "$TRAVIS_BRANCH" == "master" ]
    then "$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=medium
    fi
    if [ "$TRAVIS_BRANCH" == "master" ]
    then "$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=large
    fi
fi