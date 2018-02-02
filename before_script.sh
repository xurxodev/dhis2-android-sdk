#!/usr/bin/env bash
. has_to_execute_integration.sh

if [ "$EXECUTE_INTEGRATION" == "true" ]
then
./travis_create_avd.sh &
fi