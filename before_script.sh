#!/usr/bin/env bash
. has_to_execute_integration.sh

if [ "$TEST_SUITE" != "units" ]
then
echo no | android create avd --force -n test -t android-22 --abi armeabi-v7a
emulator -avd test -no-audio -no-window &
android-wait-for-emulator
sleep 180
adb devices
adb shell input keyevent 82 &
fi