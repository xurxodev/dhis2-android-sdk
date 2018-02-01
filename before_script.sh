#!/usr/bin/env bash

if [ "$EXECUTE_INTEGRATION" == "true" ]
then
./travis_create_avd.sh &
fi