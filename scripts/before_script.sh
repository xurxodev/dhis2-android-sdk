#!/usr/bin/env bash

if [ "$TEST_SUITE" == "integration" ]
then
./scripts/travis_create_avd.sh &
fi
if [ "$TEST_SUITE" == "integration_large" ]
then
if [ "$TRAVIS_PULL_REQUEST" == "false" ] &&  [ "$TRAVIS_BRANCH" == "development" ] || [ "$TRAVIS_BRANCH" == "master" ]
    then
    ./scripts/travis_create_avd.sh &
    fi
fi