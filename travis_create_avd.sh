#!/usr/bin/env bash

# Starting emulators is very costly. We should only start them if we're building a matrix
# component which requires one. We start the travis_create_avd.sh in the background because
# we can get a small performance improvement by continuing the build, and only blocking and
# waiting for the emulator when we absolutely need it.

if [ "$TEST_SUITE" == "instrumentation-provider" ] || [ "$TEST_SUITE" == "instrumentation-sdk" ]; then
    echo "Starting AVD for component $TEST_SUITE"
    ./scripts/travis_create_avd.sh &
fi