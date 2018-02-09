#!/usr/bin/env bash

android-wait-for-emulator
sleep 180
adb devices
adb shell input keyevent 82 &