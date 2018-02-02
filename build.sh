#!/bin/bash
set -xe
. has_to_execute_integration.sh

# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/

# This will: compile the project, run lint, run tests under JVM, package apk, check the code quality and run tests on the device/emulator.
if [ "$TEST_SUITE" == "units" ];
then
"$PROJECT_DIR"/gradlew clean
"$PROJECT_DIR"/gradlew build -Dscan; fi

if [ "$EXECUTE_INTEGRATION" == "true" ]
then
android-wait-for-emulator
sleep 180
adb devices
adb shell input keyevent 82 &
    if [ "$TEST_SUITE" == "integration" ]
    then
    "$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small
    "$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=medium
    fi
    if [ "$TEST_SUITE" == "integration_large" ]
    then
    "$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=large
    fi
fi

