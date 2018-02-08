#!/bin/bash
set -xe

android-wait-for-emulator
sleep 180
adb devices
adb shell input keyevent 82 &

if [ "$TEST_SUITE" == "integration" ]
then
"$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=small
fi
if [ "$TEST_SUITE" == "integration_large" ]
then
"$PROJECT_DIR"/gradlew connectedAndroidTest -Pandroid.testInstrumentationRunnerArguments.size=medium
fi

