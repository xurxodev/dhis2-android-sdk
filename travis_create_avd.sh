#!/usr/bin/env bash

 echo no | android create avd --force -n test -t android-27 --abi armeabi-v7a
 emulator -avd test -no-audio -no-window &
