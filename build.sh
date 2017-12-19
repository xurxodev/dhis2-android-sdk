#!/bin/bash
set -xe
size='small'

while getopts ":s:" opt; do
    case $opt in
    s) size="${OPTARG}"
    ;;
    esac
done


# You can run it from any directory.
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
PROJECT_DIR=$DIR/

# This will: compile the project, run lint, run tests under JVM, package apk, check the code quality and run tests on the device/emulator.
"$PROJECT_DIR"/gradlew --no-daemon clean
"$PROJECT_DIR"/gradlew --no-daemon build -Dscan
"$PROJECT_DIR"/gradlew --no-daemon test
"$PROJECT_DIR"/gradlew --no-daemon connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small
if [ "$size" == "medium" ] || [ "$size" == "large" ];
then "$PROJECT_DIR"/gradlew --no-daemon connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=medium; fi
if [ "$size" == "large" ];
then "$PROJECT_DIR"/gradlew --no-daemon connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=large; fi